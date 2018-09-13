package org.kizombadev.eventstudio.common.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetStats;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

@Service
public class ElasticsearchService {
    private static final String DEFAULT_DOC_TYPE = "_doc";
    private final RestHighLevelClient client;
    private final ElasticsearchProperties elasticsearchProperties;
    private TransportClient transportClient;

    @Autowired
    public ElasticsearchService(RestHighLevelClient client, ElasticsearchProperties elasticsearchProperties, TransportClient transportClient) {
        this.client = client;
        this.elasticsearchProperties = elasticsearchProperties;
        this.transportClient = transportClient;
    }

    public List<Map<String, Object>> getElementsByFilter(List<FilterCriteriaDto> filters, Integer from, Integer size) {

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(createBoolFilter(filters, FilterType.PRIMARY));
        searchSourceBuilder.size(size);
        searchSourceBuilder.from(from);
        searchSourceBuilder.sort(new FieldSortBuilder(EventKeys.TIMESTAMP.getValue()).order(SortOrder.DESC));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = executeAndValidateRequest(searchRequest);

        List<Map<String, Object>> result = new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits()) {
            result.add(hit.getSourceAsMap());
        }
        return result;
    }

    public List<Map<String, String>> getFieldStructure() {
        GetMappingsRequest request = new GetMappingsRequest();
        request.indices(elasticsearchProperties.getIndexName());
        request.types(DEFAULT_DOC_TYPE);

        GetMappingsResponse getMappingResponse;
        try {
            getMappingResponse = client.indices().getMapping(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = getMappingResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> defaultMapping = mappings.get(elasticsearchProperties.getIndexName());
        MappingMetaData ping1 = defaultMapping.get(DEFAULT_DOC_TYPE);
        Map<String, Object> sourceAsMap = ping1.getSourceAsMap();
        Map<String, Map<String, Object>> properties = (Map<String, Map<String, Object>>) sourceAsMap.get("properties");

        List<Map<String, String>> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, Object>> entry : properties.entrySet()) {
            Map<String, String> map = new HashMap<>();
            map.put("field", entry.getKey());
            map.put("type", entry.getValue().get("type").toString());
            result.add(map);
        }

        return result;
    }

    public List<Map<String, Object>> getDateHistogram(@NotNull List<FilterCriteriaDto> filters) {
        final String primaryFilter = "primaryFilter";
        final String secondaryFilter = "secondaryFilter";
        final String dateGrouping = "dateGrouping";

        FiltersAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filters(primaryFilter,
                        createBoolFilter(filters, FilterType.PRIMARY))
                .subAggregation(AggregationBuilders
                        .dateHistogram(dateGrouping)
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .field(EventKeys.TIMESTAMP.getValue())
                        .format("yyyy-MM-dd").subAggregation(
                                AggregationBuilders.filters(secondaryFilter,
                                        createBoolFilter(filters, FilterType.SECONDARY))));

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(filterAggregationBuilder);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = executeAndValidateRequest(searchRequest);

        List<Map<String, Object>> result = new ArrayList<>();

        Filters responseFilter = searchResponse.getAggregations().get(primaryFilter);
        Filters.Bucket bucket = responseFilter.getBuckets().get(0);
        Histogram aggregation = bucket.getAggregations().get(dateGrouping);
        for (Histogram.Bucket item : aggregation.getBuckets()) {

            Map<String, Object> map = new HashMap<>();
            map.put("key", item.getKeyAsString());
            map.put("primary_count", item.getDocCount());
            Filters f = item.getAggregations().get(secondaryFilter);
            Filters.Bucket bucket1 = f.getBuckets().get(0);
            map.put("secondary_count", bucket1.getDocCount());
            result.add(map);
        }

        return result;
    }

    public List<Map<String, Object>> getTermDiagram(@NotNull List<FilterCriteriaDto> filters, @NotNull EventKeys termName, @NotNull Integer count) {
        final String primary_filter = "primary_filter";
        final String terms_grouping = "terms";

        FiltersAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filters(primary_filter, createBoolFilter(filters, FilterType.PRIMARY))
                .subAggregation(AggregationBuilders.terms(terms_grouping).field(termName.getValue()).size(count));

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(filterAggregationBuilder);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = executeAndValidateRequest(searchRequest);

        List<Map<String, Object>> result = new ArrayList<>();

        Filters responseFilter = searchResponse.getAggregations().get(primary_filter);
        Filters.Bucket bucket = responseFilter.getBuckets().get(0);
        Terms aggregation = bucket.getAggregations().get(terms_grouping);
        for (Terms.Bucket item : aggregation.getBuckets()) {

            Map<String, Object> map = new HashMap<>();
            map.put("key", item.getKeyAsString());
            map.put("count", item.getDocCount());
            result.add(map);
        }

        return result;
    }

    public double getMaxValue(@NotNull List<FilterCriteriaDto> filters, @NotNull EventKeys field) {
        final String primaryFilter = "primary_filter";
        final String maxAggregation = "max_aggregation";

        FiltersAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filters(primaryFilter,
                        createBoolFilter(filters, FilterType.PRIMARY))
                .subAggregation(AggregationBuilders.max(maxAggregation).field(field.getValue()));

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(filterAggregationBuilder);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = executeAndValidateRequest(searchRequest);

        Filters responseFilter = searchResponse.getAggregations().get(primaryFilter);
        Filters.Bucket bucket = responseFilter.getBuckets().get(0);
        Max max = bucket.getAggregations().get(maxAggregation);
        return max.getValue();
    }

    //TODO migrate to the restHighLevelClient
    public void updateField(@NotNull List<FilterCriteriaDto> filters, @NotNull EventKeys field, @NotNull String value) {
        UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(transportClient);
        updateByQuery.source(elasticsearchProperties.getIndexName())
                .filter(createBoolFilter(filters, FilterType.PRIMARY))
                .script(new Script(ScriptType.INLINE, "painless", "ctx._source." + field + " = params.value", Collections.singletonMap("value", value)));


        BulkByScrollResponse response = updateByQuery.get();
        if (!response.getBulkFailures().isEmpty()) {
            throw new ElasticsearchException("The update failed");
        }
    }

    //TODO migrate to the restHighLevelClient
    public long deleteEventsOfDate(LocalDate localDate) {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(transportClient)
                .filter(QueryBuilders
                        .rangeQuery(EventKeys.TIMESTAMP.getValue())
                        .lt(localDate.plusDays(1).toString()))
                .source(elasticsearchProperties.getIndexName())
                .get();

        return response.getDeleted();
    }

    //TODO migrate to the restHighLevelClient -
    public long getIndexSizeInMb() {

        IndicesStatsResponse stats = transportClient.admin().indices().prepareStats()
                .clear()
                .setIndices(elasticsearchProperties.getIndexName())
                .setStore(true)
                .execute().actionGet();

        ByteSizeValue size = stats.getIndex(elasticsearchProperties.getIndexName()).getTotal().getStore().getSize();
        return size.getMb();
    }

    public void prepareIndex() {
        try {
            GetIndexRequest request = new GetIndexRequest();
            request.indices(elasticsearchProperties.getIndexName());
            boolean indexExists = client.indices().exists(request, RequestOptions.DEFAULT);

            if (!indexExists) {
                createIndex();
            }
        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }
    }

    public void prepareMappingField(EventKeys field, MappingType type) {

        PutMappingRequest request = new PutMappingRequest(elasticsearchProperties.getIndexName());
        request.type(DEFAULT_DOC_TYPE);
        request.source("{\n" +
                "  \"properties\": {\n" +
                "    \"" + field + "\": {\n" +
                "\t\t\"type\": \"" + type + "\"\n" +
                "\t }\n" +
                "  }\n" +
                "}", XContentType.JSON);

        PutMappingResponse putMappingResponse;
        try {
            putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }

        if (!putMappingResponse.isAcknowledged()) {
            throw new ElasticsearchException("prepare mapping failed");
        }
    }

    public void bulkInsert(List<Map<EventKeys, Object>> documents) {
        String indexName = elasticsearchProperties.getIndexName();

        BulkRequest request = new BulkRequest();

        for (Map<EventKeys, Object> document : documents) {
            Map<String, Object> item = new HashMap<>(document.size());
            for (Map.Entry<EventKeys, Object> entry : document.entrySet()) {
                item.put(entry.getKey().getValue(), entry.getValue());
            }
            request.add(new IndexRequest(indexName, ElasticsearchService.DEFAULT_DOC_TYPE).source(item));
        }

        BulkResponse bulkResponse;
        try {
            bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }

        if (bulkResponse.hasFailures()) {
            throw new ElasticsearchException(bulkResponse.buildFailureMessage());
        }
    }

    private void createIndex() {
        try {
            CreateIndexRequest request = new CreateIndexRequest(elasticsearchProperties.getIndexName());
            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            if (!createIndexResponse.isAcknowledged()) {
                throw new ElasticsearchException("the index creation failed");
            }
        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }
    }

    private QueryBuilder createBoolFilter(List<FilterCriteriaDto> filters, FilterType expectedType) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        for (FilterCriteriaDto dto : filters) {
            if (!Objects.equals(expectedType, dto.getType())) {
                continue;
            }

            if (FilterOperation.EQUALS.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.termQuery(dto.getField().getValue(), dto.getValue()));
            } else if (FilterOperation.GREATER_THEN.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.rangeQuery(dto.getField().getValue()).gt(dto.getValue()));
            } else if (FilterOperation.GREATER_THEN_OR_EQUAL.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.rangeQuery(dto.getField().getValue()).gte(dto.getValue()));
            } else if (FilterOperation.LESS_THEN.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.rangeQuery(dto.getField().getValue()).lt(dto.getValue()));
            } else if (FilterOperation.LESS_THEN_OR_EQUAL.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.rangeQuery(dto.getField().getValue()).lte(dto.getValue()));
            } else if (FilterOperation.NOT_EXIST.equals(dto.getOperator())) {
                queryBuilder.mustNot(QueryBuilders.existsQuery(dto.getField().getValue()));
            } else if (FilterOperation.EXIST.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.existsQuery(dto.getField().getValue()));
            } else if (FilterOperation.CONTAINS.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.matchQuery(dto.getField().getValue(), dto.getValue()));
            } else {
                throw new ElasticsearchException(String.format("The filter operation '%s' is unknown.", dto.getOperator()));
            }
        }

        return queryBuilder;
    }

    private void checkResponse(AcknowledgedResponse response, String message) {
        if (!response.isAcknowledged()) {
            throw new ElasticsearchException(message);
        }
    }

    private SearchResponse executeAndValidateRequest(SearchRequest searchRequest) {
        SearchResponse searchResponse;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new ElasticsearchException(e);
        }

        if (searchResponse.status() != RestStatus.OK) {
            throw new ElasticsearchException(String.format("The rest status %s is invalid", searchResponse.status()));
        }

        return searchResponse;
    }
}



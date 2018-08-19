package org.kizombadev.eventstudio.common.elasticsearch;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
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
import org.elasticsearch.search.sort.SortOrder;
import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
public class ElasticsearchService {
    private static final String DEFAULT_DOC_TYPE = "_doc";
    private final TransportClient transportClient;
    private final ElasticsearchProperties elasticsearchProperties;

    @Autowired
    public ElasticsearchService(TransportClient transportClient, ElasticsearchProperties elasticsearchProperties) {
        this.transportClient = transportClient;
        this.elasticsearchProperties = elasticsearchProperties;
    }

    public List<Map<String, Object>> getElementsByFilter(List<FilterCriteriaDto> filters, Integer from, Integer size) {
        SearchResponse searchResponse = transportClient.prepareSearch(elasticsearchProperties.getIndexName())
                .setSize(size)
                .setFrom(from)
                .setQuery(createBoolFilter(filters, FilterType.PRIMARY))
                .addSort(EventKeys.TIMESTAMP, SortOrder.DESC)
                .get();

        List<Map<String, Object>> result = new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits()) {
            result.add(hit.getSourceAsMap());
        }
        return result;
    }

    public List<Map<String, String>> getFieldStructure() {
        GetMappingsResponse getMappingsResponse = transportClient.admin().indices().prepareGetMappings(elasticsearchProperties.getIndexName()).get();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = getMappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> defaultMapping = mappings.get(elasticsearchProperties.getIndexName());
        MappingMetaData ping1 = defaultMapping.get("_doc");
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
        final String primary_filter = "primary_filter";
        final String secondary_filter = "secondary_filter";
        final String date_grouping = "date_grouping";


        FiltersAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filters(primary_filter,
                        createBoolFilter(filters, FilterType.PRIMARY))
                .subAggregation(AggregationBuilders
                        .dateHistogram(date_grouping)
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .field(EventKeys.TIMESTAMP)
                        .format("dd-MM-yyyy").subAggregation(
                                AggregationBuilders.filters(secondary_filter,
                                        createBoolFilter(filters, FilterType.SECONDARY))));

        SearchResponse searchResponse = transportClient.prepareSearch(elasticsearchProperties.getIndexName())
                .addAggregation(filterAggregationBuilder)
                .setSize(0)
                .get();

        List<Map<String, Object>> result = new ArrayList<>();

        Filters responseFilter = searchResponse.getAggregations().get(primary_filter);
        Filters.Bucket bucket = responseFilter.getBuckets().get(0);
        Histogram aggregation = bucket.getAggregations().get(date_grouping);
        for (Histogram.Bucket item : aggregation.getBuckets()) {

            Map<String, Object> map = new HashMap<>();
            map.put("key", item.getKeyAsString());
            map.put("primary_count", item.getDocCount());
            Filters f = item.getAggregations().get(secondary_filter);
            Filters.Bucket bucket1 = f.getBuckets().get(0);
            map.put("secondary_count", bucket1.getDocCount());
            result.add(map);
        }

        return result;
    }

    public List<Map<String, Object>> getTermDiagram(@NotNull List<FilterCriteriaDto> filters, @NotNull String termName, @NotNull Integer count) {
        final String primary_filter = "primary_filter";
        final String terms_grouping = "terms";

        FiltersAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filters(primary_filter,
                        createBoolFilter(filters, FilterType.PRIMARY))
                .subAggregation(AggregationBuilders.terms(terms_grouping).field(termName).size(count));

        SearchResponse searchResponse = transportClient.prepareSearch(elasticsearchProperties.getIndexName())
                .addAggregation(filterAggregationBuilder)
                .setSize(0)
                .get();

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

    public double getMaxValue(@NotNull List<FilterCriteriaDto> filters, @NotNull String field) {
        final String primaryFilter = "primary_filter";
        final String maxAggregation = "max_aggregation";

        FiltersAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filters(primaryFilter,
                        createBoolFilter(filters, FilterType.PRIMARY))
                .subAggregation(AggregationBuilders.max(maxAggregation).field(field));

        SearchResponse searchResponse = transportClient.prepareSearch(elasticsearchProperties.getIndexName())
                .addAggregation(filterAggregationBuilder)
                .setSize(0)
                .get();

        Filters responseFilter = searchResponse.getAggregations().get(primaryFilter);
        Filters.Bucket bucket = responseFilter.getBuckets().get(0);
        Max max = bucket.getAggregations().get(maxAggregation);
        return max.getValue();
    }

    public void updateField(@NotNull List<FilterCriteriaDto> filters, @NotNull String field, @NotNull String value) {
        UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(transportClient);
        updateByQuery.source(elasticsearchProperties.getIndexName())
                .filter(createBoolFilter(filters, FilterType.PRIMARY))
                .script(new Script(ScriptType.INLINE, "painless", "ctx._source." + field + " = params.value", Collections.singletonMap("value", value)));


        BulkByScrollResponse response = updateByQuery.get();
        if (!response.getBulkFailures().isEmpty()) {
            throw new IllegalStateException("The update failed");
        }
    }

    public long deleteEvents(int numberOfDays) {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(transportClient)
                .filter(QueryBuilders
                        .rangeQuery(EventKeys.TIMESTAMP)
                        .lt(LocalDate.now().minus(Period.ofDays(numberOfDays)).toString()))
                .source(elasticsearchProperties.getIndexName())
                .get();

        return response.getDeleted();
    }

    public void prepareIndex() {
        GetIndexResponse response = transportClient.admin().indices().prepareGetIndex().get();

        boolean indexExist = Arrays.stream(response.getIndices()).anyMatch(x -> x.equals(elasticsearchProperties.getIndexName()));
        if (!indexExist) {
            createIndex();
        }
    }

    public void prepareMappingField(String field, String type) {

                PutMappingResponse response = transportClient
                .admin()
                .indices()
                .preparePutMapping(elasticsearchProperties.getIndexName())
                .setType(DEFAULT_DOC_TYPE)
                .setSource("{\n" +
                        "  \"properties\": {\n" +
                        "    \"" + field + "\": {\n" +
                        "\t\t\"type\": \"" + type + "\"\n" +
                        "\t }\n" +
                        "  }\n" +
                        "}", XContentType.JSON).get();
        checkResponse(response, "prepare mapping failed");
    }

    public void bulkInsert(List<Map<String, Object>> documents) {
        String indexName = elasticsearchProperties.getIndexName();

        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();

        for (Map<String, Object> document : documents) {
            bulkRequest.add(transportClient.prepareIndex(indexName, ElasticsearchService.DEFAULT_DOC_TYPE).setSource(document));
        }

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            throw new IllegalStateException(bulkResponse.buildFailureMessage());
        }
    }

    private void createIndex() {
        CreateIndexResponse response = transportClient.admin().indices().prepareCreate(elasticsearchProperties.getIndexName()).get();
        checkResponse(response, "the index creation failed");
    }

    private QueryBuilder createBoolFilter(List<FilterCriteriaDto> filters, String expectedType) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        for (FilterCriteriaDto dto : filters) {
            if (!Objects.equals(expectedType, dto.getType())) {
                continue;
            }

            if (FilterOperation.EQUALS.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.termQuery(dto.getField(), dto.getValue()));
            } else if (FilterOperation.GREATER_THEN.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.rangeQuery(dto.getField()).gt(dto.getValue()));
            } else if (FilterOperation.GREATER_THEN_OR_EQUAL.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.rangeQuery(dto.getField()).gte(dto.getValue()));
            } else if (FilterOperation.LESS_THEN.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.rangeQuery(dto.getField()).lt(dto.getValue()));
            } else if (FilterOperation.LESS_THEN_OR_EQUAL.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.rangeQuery(dto.getField()).lte(dto.getValue()));
            } else if (FilterOperation.NOT_EXIST.equals(dto.getOperator())) {
                queryBuilder.mustNot(QueryBuilders.existsQuery(dto.getField()));
            } else if (FilterOperation.EXIST.equals(dto.getOperator())) {
                queryBuilder.must(QueryBuilders.existsQuery(dto.getField()));
            } else {
                throw new IllegalStateException(String.format("The filter operation '%s' is unknown.", dto.getOperator()));
            }
        }

        return queryBuilder;
    }

    private void checkResponse(AcknowledgedResponse response, String message) {
        if (!response.isAcknowledged()) {
            throw new IllegalStateException(message);
        }
    }
}



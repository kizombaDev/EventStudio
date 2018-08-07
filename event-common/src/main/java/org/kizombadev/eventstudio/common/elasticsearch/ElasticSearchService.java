package org.kizombadev.eventstudio.common.elasticsearch;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
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
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.*;

@Service
public class ElasticSearchService {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TransportClient transportClient;
    private final ElasticsearchProperties elasticsearchProperties;

    @Autowired
    public ElasticSearchService(TransportClient transportClient, ElasticsearchProperties elasticsearchProperties) {
        this.transportClient = transportClient;
        this.elasticsearchProperties = elasticsearchProperties;
    }

    public List<Map<String, Object>> getElementsByFilter(List<FilterCriteriaDto> filters, Integer from, Integer size) {
        SearchResponse searchResponse = transportClient.prepareSearch(elasticsearchProperties.getIndexName())
                .setSize(size)
                .setFrom(from)
                .setQuery(createBoolFilter(filters, "primary"))
                .addSort("timestamp", SortOrder.DESC)
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
        ImmutableOpenMap<String, MappingMetaData> ping = mappings.get(elasticsearchProperties.getIndexName());
        MappingMetaData ping1 = ping.get("ping");
        Map<String, Object> sourceAsMap = ping1.getSourceAsMap();
        Map<String, Map<String, Object>> properties = (Map<String, Map<String, Object>>) sourceAsMap.get("properties");

        List<Map<String, String>> result = new ArrayList<>();

        for (String fieldName : properties.keySet()) {
            Map<String, String> map = new HashMap<>();
            map.put("field", fieldName);
            map.put("type", properties.get(fieldName).get("type").toString());
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
                        createBoolFilter(filters, "primary"))
                .subAggregation(AggregationBuilders
                        .dateHistogram(date_grouping)
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .field("timestamp")
                        .format("dd-MM-yyyy").subAggregation(
                                AggregationBuilders.filters(secondary_filter,
                                        createBoolFilter(filters, "secondary"))));

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
                        createBoolFilter(filters, "primary"))
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

    public void updateField(@NotNull List<FilterCriteriaDto> filters, @NotNull String field, @NotNull String value) {
        UpdateByQueryRequestBuilder updateByQuery = UpdateByQueryAction.INSTANCE.newRequestBuilder(transportClient);
        updateByQuery.source(elasticsearchProperties.getIndexName())
                .filter(createBoolFilter(filters, "primary"))
                .script(new Script(ScriptType.INLINE, "painless",  "ctx._source." + field + " = params.value", Collections.singletonMap("value", value)));


        BulkByScrollResponse response = updateByQuery.get();
        if (!response.getBulkFailures().isEmpty()) {
           throw new IllegalStateException("The update failed");
        }
    }

    private QueryBuilder createBoolFilter(List<FilterCriteriaDto> filters, String expectedType) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        for (FilterCriteriaDto dto : filters) {
            if (!Objects.equals(expectedType, dto.getType())) {
                continue;
            }

            switch (dto.getOperator()) {
                case "equals":
                    queryBuilder.must(QueryBuilders.termQuery(dto.getField(), dto.getValue()));
                    break;
                case "gt":
                    queryBuilder.must(QueryBuilders.rangeQuery(dto.getField()).gt(dto.getValue()));
                    break;
                case "gte":
                    queryBuilder.must(QueryBuilders.rangeQuery(dto.getField()).gte(dto.getValue()));
                    break;
                case "lt":
                    queryBuilder.must(QueryBuilders.rangeQuery(dto.getField()).lt(dto.getValue()));
                    break;
                case "lte":
                    queryBuilder.must(QueryBuilders.rangeQuery(dto.getField()).lte(dto.getValue()));
                    break;
                case "not_exist":
                    queryBuilder.mustNot(QueryBuilders.existsQuery(dto.getField()));
                    break;
                case "exist":
                    queryBuilder.must(QueryBuilders.existsQuery(dto.getField()));
                    break;
                default:
                    throw new IllegalStateException(String.format("The filter operation '%s' is unknown.", dto.getOperator()));
            }
        }

        return queryBuilder;
    }

}

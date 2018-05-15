package org.kizombadev.app.web.backend;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.sort.SortOrder;
import org.kizombadev.app.web.backend.model.FilterCriteriaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchService {

    private final TransportClient transportClient;
    private BackendProperties backendProperties;

    @Autowired
    public ElasticSearchService(TransportClient transportClient, BackendProperties backendProperties) {
        this.transportClient = transportClient;
        this.backendProperties = backendProperties;
    }

    public List<Map<String, Object>> getElementsByFilter(List<FilterCriteriaDto> filters, Integer from, Integer size) {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        filters.stream().forEach(x -> queryBuilder.must(QueryBuilders.termQuery(x.getField(), x.getValue())));

        SearchResponse searchResponse = transportClient.prepareSearch(backendProperties.getIndexName())
                .setSize(size)
                .setFrom(from)
                .setQuery(queryBuilder)
                .addSort("timestamp", SortOrder.DESC)
                .get();

        List<Map<String, Object>> result = new ArrayList<>();

        for (SearchHit hit : searchResponse.getHits()) {
            result.add(hit.getSourceAsMap());
        }
        return result;
    }

    public List<Map<String, String>> getFieldStructure() {
        GetMappingsResponse getMappingsResponse = transportClient.admin().indices().prepareGetMappings(backendProperties.getIndexName()).get();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> mappings = getMappingsResponse.getMappings();
        ImmutableOpenMap<String, MappingMetaData> ping = mappings.get(backendProperties.getIndexName());
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

        BoolQueryBuilder primaryFilter = QueryBuilders.boolQuery();

        filters.stream().
                filter(x -> "primary".equals(x.getType())).
                forEach(x -> primaryFilter.must(QueryBuilders.termQuery(x.getField(), x.getValue())));

        BoolQueryBuilder secondaryFilter = QueryBuilders.boolQuery();

        filters.stream().filter(x -> "secondary".equals(x.getType())).
                forEach(x -> secondaryFilter.must(QueryBuilders.termQuery(x.getField(), x.getValue())));

        FiltersAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filters(primary_filter, primaryFilter)
                .subAggregation(AggregationBuilders
                        .dateHistogram(date_grouping)
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .field("timestamp")
                        .format("dd-MM-yyyy").subAggregation(
                                AggregationBuilders.filters(secondary_filter,
                                        secondaryFilter)));

        SearchResponse searchResponse = transportClient.prepareSearch(backendProperties.getIndexName())
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
}

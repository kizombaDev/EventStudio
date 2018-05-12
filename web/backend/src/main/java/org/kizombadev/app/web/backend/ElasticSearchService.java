package org.kizombadev.app.web.backend;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.kizombadev.app.web.backend.model.FilterCriteriaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        for(FilterCriteriaDto dto : filters) {
            queryBuilder.must(QueryBuilders.termQuery(dto.getField(), dto.getValue()));
        }

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
}

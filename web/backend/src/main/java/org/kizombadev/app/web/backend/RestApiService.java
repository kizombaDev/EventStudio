package org.kizombadev.app.web.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/rest/v1/logs")
public class RestApiService {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final TransportClient transportClient;

    @Autowired
    public RestApiService(TransportClient transportClient) {
        this.transportClient = transportClient;
    }

    @RequestMapping(path = "/{id}/last", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getLastElementById(@PathVariable String id) throws ExecutionException, InterruptedException {

        SearchResponse searchResponse = transportClient.prepareSearch("ping2")
                .setSize(1)
                .setQuery(QueryBuilders.termQuery("id", id))
                .addSort("timestamp", SortOrder.DESC)
                .execute()
                .get();

        if (searchResponse.getHits().getHits().length == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        SearchHit searchHit = searchResponse.getHits().getAt(0);
        return new ResponseEntity<>(searchHit.getSource(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getFieldValuesByType(@RequestParam("type") String type, @RequestParam("group-by") String groupBy) throws ExecutionException, InterruptedException {


        final String filter_identifier = "Type_Filter";
        final String group_by_field_identifier = "group_by_field";

        FilterAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filter(filter_identifier, QueryBuilders
                        .termQuery("type", type))
                .subAggregation(AggregationBuilders
                        .terms(group_by_field_identifier)
                        .field(groupBy));

        SearchResponse searchResponse = transportClient.prepareSearch("ping2")
                .addAggregation(filterAggregationBuilder)
                .setSize(0)
                .execute()
                .get();

        Filter filter = searchResponse.getAggregations().get(filter_identifier);
        StringTerms stringTerms = filter.getAggregations().get(group_by_field_identifier);
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();

        List<Map<String, Object>> result = new ArrayList<>(buckets.size());

        for (StringTerms.Bucket entry : buckets) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("key", entry.getKeyAsString());
            map.put("count", entry.getDocCount());
            result.add(map);
        }

        if (result.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
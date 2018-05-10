package org.kizombadev.app.web.backend;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.filter.Filters;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/logs")
public class RestApiService {

    private final TransportClient transportClient;

    @Autowired
    public RestApiService(TransportClient transportClient) {
        this.transportClient = transportClient;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getLastElementById(@PathVariable String id, @RequestParam("from") @NotNull Integer from, @RequestParam("size") @NotNull Integer size)
            throws ExecutionException, InterruptedException {

        SearchResponse searchResponse = transportClient.prepareSearch("ping")
                .setSize(size)
                .setFrom(from)
                .setQuery(QueryBuilders.termQuery("id", id))
                .addSort("timestamp", SortOrder.DESC)
                .execute()
                .get();

        if (searchResponse.getHits().getHits().length == 0) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        SearchHit searchHit = searchResponse.getHits().getAt(0);
        return new ResponseEntity<>(searchHit.getSourceAsString(), HttpStatus.OK);
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

        SearchResponse searchResponse = transportClient.prepareSearch("ping")
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

    @RequestMapping(path = "/structure", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getTypeIdStructure() throws ExecutionException, InterruptedException {

        final String group_by_type_identifier = "group_by_type";
        final String group_by_id_identifier = "group_by_id";

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms(group_by_type_identifier).field("type")
                .subAggregation(AggregationBuilders
                        .terms(group_by_id_identifier)
                        .field("id"));

        SearchResponse searchResponse = transportClient.prepareSearch("ping")
                .addAggregation(termsAggregationBuilder)
                .setSize(0)
                .execute()
                .get();

        List<Map<String, Object>> result = new ArrayList<>();

        StringTerms typeTerms = searchResponse.getAggregations().get(group_by_type_identifier);
        for (StringTerms.Bucket typeBucket : typeTerms.getBuckets()) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", typeBucket.getKeyAsString());

            StringTerms idTerms = typeBucket.getAggregations().get(group_by_id_identifier);
            List<String> ids = new ArrayList<>(idTerms.getBuckets().size());
            for (StringTerms.Bucket entry : idTerms.getBuckets()) {
                ids.add(entry.getKeyAsString());
            }
            map.put("ids", ids);
            result.add(map);
        }

        if (result.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(path = "/date-histogram", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getDateHistogram(@RequestBody List<Map<String, String>> filters) throws ExecutionException, InterruptedException {
        final String primary_filter = "primary_filter";
        final String secondary_filter = "secondary_filter";
        final String date_grouping = "date_grouping";

        BoolQueryBuilder primaryFilter = QueryBuilders.boolQuery();

        filters.stream().filter(x -> x.get("type").equals("primary")).
                forEach(x -> primaryFilter.must(QueryBuilders.termQuery(x.get("field"), x.get("value"))));

        BoolQueryBuilder secondaryFilter = QueryBuilders
                .boolQuery();

        filters.stream().filter(x -> x.get("type").equals("secondary")).
                forEach(x -> secondaryFilter.must(QueryBuilders.termQuery(x.get("field"), x.get("value"))));

        FiltersAggregationBuilder filterAggregationBuilder = AggregationBuilders
                .filters(primary_filter, primaryFilter)
                .subAggregation(AggregationBuilders
                        .dateHistogram(date_grouping)
                        .dateHistogramInterval(DateHistogramInterval.DAY)
                        .field("timestamp")
                        .format("dd-MM-yyyy").subAggregation(
                                AggregationBuilders.filters(secondary_filter,
                                        secondaryFilter)));

        SearchResponse searchResponse = transportClient.prepareSearch("ping")
                .addAggregation(filterAggregationBuilder)
                .setSize(0)
                .execute()
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

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
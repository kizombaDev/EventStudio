package org.kizombadev.eventstudio.apps.restapiapp;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.Filter;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.kizombadev.eventstudio.apps.restapiapp.model.FilterCriteriaDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/events")
public class RestApiService {

    private final TransportClient transportClient;
    private ElasticSearchService elasticSearchService;

    @Autowired
    public RestApiService(TransportClient transportClient, ElasticSearchService elasticSearchService) {
        this.transportClient = transportClient;
        this.elasticSearchService = elasticSearchService;
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getElements(@PathVariable String id, @RequestParam("from") @NotNull Integer from, @RequestParam("size") @NotNull Integer size) {

        FilterCriteriaDto filterCriteriaDto = new FilterCriteriaDto();
        filterCriteriaDto.setField("id");
        filterCriteriaDto.setValue(id);
        filterCriteriaDto.setOperator("equals");
        filterCriteriaDto.setType("primary");
        List<FilterCriteriaDto> filters = Collections.singletonList(filterCriteriaDto);
        List<Map<String, Object>> items = elasticSearchService.getElementsByFilter(filters, from, size);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getElementsByFilter(@RequestBody @NotNull List<FilterCriteriaDto> filters, @NotNull Integer from, @RequestParam("size") @NotNull Integer size) {

        List<Map<String, Object>> items = elasticSearchService.getElementsByFilter(filters, from, size);
        return new ResponseEntity<>(items, HttpStatus.OK);
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

    @RequestMapping(path = "/structure/fields", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getFields() {
        List<Map<String, String>> result = elasticSearchService.getFieldStructure();
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
    public ResponseEntity<Object> getDateHistogram(@RequestBody @NotNull List<FilterCriteriaDto> filters) {
        List<Map<String, Object>> data = elasticSearchService.getDateHistogram(filters);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
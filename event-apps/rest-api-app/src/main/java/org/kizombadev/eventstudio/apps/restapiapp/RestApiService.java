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
    public ResponseEntity<Object> getElements(@PathVariable String id,
                                              @RequestParam("from") @NotNull Integer from,
                                              @RequestParam("size") @NotNull Integer size) {

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
    public ResponseEntity<Object> getElementsByFilter(@RequestBody @NotNull List<FilterCriteriaDto> filters,
                                                      @RequestParam("from") @NotNull Integer from,
                                                      @RequestParam("size") @NotNull Integer size) {

        List<Map<String, Object>> items = elasticSearchService.getElementsByFilter(filters, from, size);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getFieldValuesByType(@RequestParam("type") String type,
                                                       @RequestParam("group-by") String groupBy) {

        FilterCriteriaDto filterCriteriaDto = new FilterCriteriaDto();
        filterCriteriaDto.setField("type");
        filterCriteriaDto.setValue(type);
        filterCriteriaDto.setOperator("equals");
        filterCriteriaDto.setType("primary");
        List<FilterCriteriaDto> filters = Collections.singletonList(filterCriteriaDto);
        List<Map<String, Object>> data = elasticSearchService.getTermDiagram(filters, groupBy, 99999);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(path = "/structure/fields", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getFields() {
        List<Map<String, String>> result = elasticSearchService.getFieldStructure();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(path = "/structure", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getTypeIdStructure() {

        List<Map<String, Object>> data = elasticSearchService.getTermDiagram(new ArrayList<>(),"type", 99999);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(path = "/date-histogram", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getDateHistogram(@RequestBody @NotNull List<FilterCriteriaDto> filters) {
        List<Map<String, Object>> data = elasticSearchService.getDateHistogram(filters);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @RequestMapping(path = "/term-diagram", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getTermDiagram(@RequestBody @NotNull List<FilterCriteriaDto> filters,
                                                   @RequestParam("term-name") @NotNull String termName,
                                                   @RequestParam("count") @NotNull Integer count) {
        List<Map<String, Object>> data = elasticSearchService.getTermDiagram(filters, termName, count);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
package org.kizombadev.eventstudio.apps.analysisapp;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.kizombadev.eventstudio.common.elasticsearch.FilterCriteriaDto;
import org.kizombadev.eventstudio.common.elasticsearch.FilterOperation;
import org.kizombadev.eventstudio.common.elasticsearch.FilterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events")
public class RestApiService {

    private ElasticsearchService elasticSearchService;

    @Autowired
    public RestApiService(ElasticsearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @GetMapping(path = "/{sourceId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getElements(@PathVariable("sourceId") String sourceId,
                                              @RequestParam("from") @NotNull Integer from,
                                              @RequestParam("size") @NotNull Integer size) {

        FilterCriteriaDto filterCriteriaDto = new FilterCriteriaDto();
        filterCriteriaDto.setField(EventKeys.SOURCE_ID);
        filterCriteriaDto.setValue(sourceId);
        filterCriteriaDto.setOperator(FilterOperation.EQUALS);
        filterCriteriaDto.setType(FilterType.PRIMARY);

        List<FilterCriteriaDto> filters = Collections.singletonList(filterCriteriaDto);
        List<Map<String, Object>> items = elasticSearchService.getElementsByFilter(filters, from, size);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getElementsByFilter(@RequestBody @NotNull List<FilterCriteriaDto> filters,
                                                      @RequestParam("from") @NotNull Integer from,
                                                      @RequestParam("size") @NotNull Integer size) {

        List<Map<String, Object>> items = elasticSearchService.getElementsByFilter(filters, from, size);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getFieldValuesByType(@RequestParam("type") String type,
                                                       @RequestParam("group-by") String groupBy) {

        FilterCriteriaDto filterCriteriaDto = new FilterCriteriaDto();
        filterCriteriaDto.setField(EventKeys.TYPE);
        filterCriteriaDto.setValue(type);
        filterCriteriaDto.setOperator(FilterOperation.EQUALS);
        filterCriteriaDto.setType(FilterType.PRIMARY);

        List<FilterCriteriaDto> filters = Collections.singletonList(filterCriteriaDto);
        List<Map<String, Object>> data = elasticSearchService.getTermDiagram(filters, EventKeys.forValue(groupBy), 99999);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping(path = "/structure/fields", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getFields() {
        List<Map<String, String>> result = elasticSearchService.getFieldStructure();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(path = "/structure", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getTypeIdStructure() {

        List<Map<String, Object>> data = elasticSearchService.getTermDiagram(new ArrayList<>(), EventKeys.TYPE, 99999);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(path = "/date-diagram", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getDateDiagram(@RequestBody @NotNull List<FilterCriteriaDto> filters) {
        List<Map<String, Object>> data = elasticSearchService.getDateDiagram(filters);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(path = "/term-diagram", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> getTermDiagram(@RequestBody @NotNull List<FilterCriteriaDto> filters,
                                                 @RequestParam("term-name") @NotNull String termName,
                                                 @RequestParam("count") @NotNull Integer count) {
        List<Map<String, Object>> data = elasticSearchService.getTermDiagram(filters, EventKeys.forValue(termName), count);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
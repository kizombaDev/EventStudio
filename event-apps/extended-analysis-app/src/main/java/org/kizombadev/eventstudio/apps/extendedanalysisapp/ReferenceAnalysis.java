package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import com.google.common.base.Strings;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.kizombadev.eventstudio.common.elasticsearch.FilterCriteriaDto;
import org.kizombadev.eventstudio.common.elasticsearch.FilterOperation;
import org.kizombadev.eventstudio.common.elasticsearch.FilterType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReferenceAnalysis implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ElasticsearchService elasticSearchService;
    private Properties properties;

    @Autowired
    public ReferenceAnalysis(ElasticsearchService elasticSearchService, Properties properties) {
        this.elasticSearchService = elasticSearchService;
        this.properties = properties;
    }

    @Override
    public void run() {
        List<Map<String, Object>> result = elasticSearchService.getTermDiagram(Arrays.asList(getTypeFilter(), getNotExistReferenceFilter()), properties.getIndicatorField(), 100);

        if (result.isEmpty()) {
            log.info("No entries for the analysis found");
        } else {
            log.info("Found {} event groups for the analysis", result.size());
        }

        result.parallelStream().map(item -> {
            String indicatorValue = item.get("key").toString();
            String referenceValue = getExistingReferenceValue(indicatorValue);

            if (referenceValue == null) {
                referenceValue = UUID.randomUUID().toString();
            }

            return new AbstractMap.SimpleEntry<>(indicatorValue, referenceValue);
        }).forEach(entry -> handleUpdate(entry.getKey(), entry.getValue()));

        log.info("Extended analysis was successfully executed");
    }

    private void handleUpdate(String ip, String value) {
        elasticSearchService.updateField(Arrays.asList(getTypeFilter(), getNotExistReferenceFilter(), getIndicatorFilter(ip)), properties.getReferenceField(), value);
    }

    private String getExistingReferenceValue(String ip) {

        List<Map<String, Object>> result = elasticSearchService.getElementsByFilter(Arrays.asList(getTypeFilter(), getExistReferenceFilter(), getIndicatorFilter(ip)), 0, 1);
        if (result.isEmpty()) {
            return null;
        }

        String value = result.get(0).get(properties.getReferenceField()).toString();
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return value;
    }

    private FilterCriteriaDto getNotExistReferenceFilter() {
        FilterCriteriaDto referenceFilter = getExistReferenceFilter();
        referenceFilter.setOperator(FilterOperation.NOT_EXIST);
        return referenceFilter;
    }

    private FilterCriteriaDto getExistReferenceFilter() {
        FilterCriteriaDto referenceFilter = new FilterCriteriaDto();
        referenceFilter.setField(properties.getReferenceField());
        referenceFilter.setValue("");
        referenceFilter.setOperator(FilterOperation.EXIST);
        referenceFilter.setType(FilterType.PRIMARY);
        return referenceFilter;
    }

    private FilterCriteriaDto getTypeFilter() {
        FilterCriteriaDto typeFilter = new FilterCriteriaDto();
        typeFilter.setField(EventKeys.TYPE);
        typeFilter.setValue(properties.getEventType());
        typeFilter.setOperator(FilterOperation.EQUALS);
        typeFilter.setType(FilterType.PRIMARY);
        return typeFilter;
    }

    private FilterCriteriaDto getIndicatorFilter(String indicatorFieldValue) {
        FilterCriteriaDto indicatorFilter = new FilterCriteriaDto();
        indicatorFilter.setField(properties.getIndicatorField());
        indicatorFilter.setValue(indicatorFieldValue);
        indicatorFilter.setOperator(FilterOperation.EQUALS);
        indicatorFilter.setType(FilterType.PRIMARY);
        return indicatorFilter;
    }
}

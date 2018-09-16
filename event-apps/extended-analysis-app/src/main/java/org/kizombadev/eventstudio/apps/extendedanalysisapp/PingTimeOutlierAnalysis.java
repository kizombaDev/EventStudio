package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PingTimeOutlierAnalysis implements Runnable {

    private static final EventKeys TIME_OUTLIER = EventKeys.forValue("time_outlier");
    private static final EventKeys FIELD_KEY_TIME = EventKeys.TIME;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ElasticsearchService elasticSearchService;
    private final Properties properties;

    @Autowired
    public PingTimeOutlierAnalysis(ElasticsearchService elasticSearchService, Properties properties) {
        this.elasticSearchService = elasticSearchService;
        this.properties = properties;
    }

    @PostConstruct
    public void prepareReferenceFieldMapping() {
        elasticSearchService.prepareIndex();
        elasticSearchService.prepareMappingField(TIME_OUTLIER, MappingType.BOOLEAN_TYPE);
    }

    @Override
    public void run() {
        List<Map<String, Object>> pings = elasticSearchService.getElementsByFilter(Arrays.asList(getTypeFilter(), getNotExistTimeOutlierFilter(), getFieldExistFilter(FIELD_KEY_TIME)), 0, 100);

        if (pings.isEmpty()) {
            log.info("No entries for the analysis found");
            return;
        }

        log.info("Found {} ping events for the analysis", pings.size());
        pings.forEach(this::analyse);
        log.info("Extended analysis was successfully executed");
    }

    private void analyse(Map<String, Object> ping) {
        final String sourceId = ping.get(EventKeys.SOURCE_ID.getValue()).toString();
        final String timestamp = ping.get(EventKeys.TIMESTAMP.getValue()).toString();
        final int time = Integer.parseInt(ping.get(EventKeys.TIME.getValue()).toString());

        List<Map<String, Object>> historicalPings = elasticSearchService.getElementsByFilter(
                Arrays.asList(getTypeFilter(),
                        getSourceIdFilter(sourceId),
                        getOlderThenTimestampFilter(timestamp),
                        getFieldExistFilter(FIELD_KEY_TIME)),
                0, properties.getMaxHistoricalEvents());

        List<Integer> historicalTimeValues = historicalPings.stream().map(item -> Integer.valueOf(item.get(FIELD_KEY_TIME.getValue()).toString())).collect(Collectors.toList());

        boolean result = OutlierCalculation.evaluate(historicalTimeValues, time, properties.getPercentile(), properties.getInterquartileFactor());
        elasticSearchService.updateField(Arrays.asList(getTypeFilter(), getSourceIdFilter(sourceId), getTimestampEqualsFilter(timestamp)), TIME_OUTLIER, String.valueOf(result));
    }

    private FilterCriteriaDto getNotExistTimeOutlierFilter() {
        FilterCriteriaDto referenceFilter = getFieldExistFilter(TIME_OUTLIER);
        referenceFilter.setOperator(FilterOperation.NOT_EXIST);
        return referenceFilter;
    }

    private FilterCriteriaDto getTypeFilter() {
        FilterCriteriaDto typeFilter = new FilterCriteriaDto();
        typeFilter.setField(EventKeys.TYPE);
        typeFilter.setValue("ping");
        typeFilter.setOperator(FilterOperation.EQUALS);
        typeFilter.setType(FilterType.PRIMARY);
        return typeFilter;
    }

    private FilterCriteriaDto getSourceIdFilter(String sourceIdValue) {
        FilterCriteriaDto typeFilter = new FilterCriteriaDto();
        typeFilter.setField(EventKeys.SOURCE_ID);
        typeFilter.setValue(sourceIdValue);
        typeFilter.setOperator(FilterOperation.EQUALS);
        typeFilter.setType(FilterType.PRIMARY);
        return typeFilter;
    }

    private FilterCriteriaDto getOlderThenTimestampFilter(String timestamp) {
        FilterCriteriaDto typeFilter = new FilterCriteriaDto();
        typeFilter.setField(EventKeys.TIMESTAMP);
        typeFilter.setValue(timestamp);
        typeFilter.setOperator(FilterOperation.LESS_THEN);
        typeFilter.setType(FilterType.PRIMARY);
        return typeFilter;
    }

    private FilterCriteriaDto getFieldExistFilter(EventKeys eventKey) {
        FilterCriteriaDto referenceFilter = new FilterCriteriaDto();
        referenceFilter.setField(eventKey);
        referenceFilter.setValue("");
        referenceFilter.setOperator(FilterOperation.EXIST);
        referenceFilter.setType(FilterType.PRIMARY);
        return referenceFilter;
    }

    private FilterCriteriaDto getTimestampEqualsFilter(String timestamp) {
        FilterCriteriaDto typeFilter = new FilterCriteriaDto();
        typeFilter.setField(EventKeys.TIMESTAMP);
        typeFilter.setValue(timestamp);
        typeFilter.setOperator(FilterOperation.EQUALS);
        typeFilter.setType(FilterType.PRIMARY);
        return typeFilter;
    }
}

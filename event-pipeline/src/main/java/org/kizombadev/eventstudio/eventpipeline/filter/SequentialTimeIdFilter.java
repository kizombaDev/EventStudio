package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.kizombadev.eventstudio.common.elasticsearch.FilterCriteriaDto;
import org.kizombadev.eventstudio.common.elasticsearch.FilterOperation;
import org.kizombadev.eventstudio.common.elasticsearch.FilterType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component("SequentialTimeIdFilter")
public class SequentialTimeIdFilter implements Filter {

    private static final Map<String, AtomicLong> ATOMIC_MAP = new HashMap<>();
    private final ElasticsearchService elasticSearchService;

    @Autowired
    public SequentialTimeIdFilter(ElasticsearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @Override
    public void handle(Map<EventKeys, Object> json) {
        String sourceId = json.get(EventKeys.SOURCE_ID).toString();
        AtomicLong atomicId = ATOMIC_MAP.get(sourceId);

        if (atomicId == null) {
            atomicId = new AtomicLong(initSequentialTimeId(sourceId));
            ATOMIC_MAP.put(sourceId, atomicId);
        }

        json.put(EventKeys.SEQUENTIAL_TIME_ID, atomicId.getAndIncrement());
    }

    @Override
    public Filter instanceCopy() {
        return new SequentialTimeIdFilter(elasticSearchService);
    }

    private long initSequentialTimeId(String sourceId) {
        FilterCriteriaDto dto = new FilterCriteriaDto();
        dto.setOperator(FilterOperation.EQUALS);
        dto.setField(EventKeys.SOURCE_ID);
        dto.setValue(sourceId);
        dto.setType(FilterType.PRIMARY);
        double maxValue = elasticSearchService.getMaxValue(Collections.singletonList(dto), EventKeys.SEQUENTIAL_TIME_ID);

        if (maxValue == Double.NEGATIVE_INFINITY) {
            return 0;
        }

        return Math.round(maxValue);
    }
}

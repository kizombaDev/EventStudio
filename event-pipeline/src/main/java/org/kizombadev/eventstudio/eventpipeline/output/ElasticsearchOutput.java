package org.kizombadev.eventstudio.eventpipeline.output;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.kizombadev.eventstudio.common.elasticsearch.MappingType;
import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.kizombadev.eventstudio.eventpipeline.FieldMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ElasticsearchOutput implements Output {

    private static final List<EventKeys> EXISTING_FIELDS = Collections.synchronizedList(new ArrayList());
    private ElasticsearchService elasticSearchService;

    @Autowired
    public ElasticsearchOutput(ElasticsearchService elasticSearchService) {
        this.elasticSearchService = elasticSearchService;
    }

    @PostConstruct
    public void init() {
        elasticSearchService.prepareIndex();
    }

    @Override
    public void write(List<EventEntry> logEntries) {
        logEntries.forEach(this::prepareMapping);
        elasticSearchService.bulkInsert(logEntries.stream().map(EventEntry::getSource).collect(Collectors.toList()));
    }

    private void prepareMapping(EventEntry data) {

        for (Map.Entry<EventKeys, Object> pair : data.getSource().entrySet()) {

            if (!FieldMapping.contains(pair.getKey())) {
                throw new IllegalStateException(String.format("for the field '%s' does not exist a mapping field definition", pair.getKey()));
            }

            prepareMappingField(pair.getKey(), FieldMapping.getTypeOfField(pair.getKey()));
        }
    }

    private void prepareMappingField(EventKeys field, MappingType type) {

        if (EXISTING_FIELDS.contains(field)) {
            return;
        }

        elasticSearchService.prepareMappingField(field, type);

        EXISTING_FIELDS.add(field);
    }
}

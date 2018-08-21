package org.kizombadev.eventstudio.eventpipeline.output;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.kizombadev.eventstudio.common.elasticsearch.FieldTypes;
import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ElasticsearchOutput implements Output {

    private static final List<String> EXISTING_FIELDS = Collections.synchronizedList(new ArrayList());
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


        //TODO extract to config file
        Map<String, String> types = new HashMap<>();
        types.put("source_id", FieldTypes.KEYWORD_TYPE);
        types.put("type", FieldTypes.KEYWORD_TYPE);
        types.put("timestamp", FieldTypes.DATE_TYPE);
        types.put("data", FieldTypes.TEXT_TYPE);

        types.put("status", FieldTypes.KEYWORD_TYPE);
        types.put("time", FieldTypes.INTEGER_TYPE);
        types.put("ttl", FieldTypes.INTEGER_TYPE);
        types.put("ip", FieldTypes.IP_TYPE);
        types.put("bytes", FieldTypes.INTEGER_TYPE);
        types.put(EventKeys.TIME_MANIPULATION, FieldTypes.BOOLEAN_TYPE);

        types.put("length", FieldTypes.INTEGER_TYPE);
        types.put("request_method", FieldTypes.KEYWORD_TYPE);
        types.put("path", FieldTypes.KEYWORD_TYPE);
        types.put("request_version", FieldTypes.KEYWORD_TYPE);
        types.put("response_status", FieldTypes.INTEGER_TYPE);
        types.put("host", FieldTypes.KEYWORD_TYPE);
        types.put("referrer", FieldTypes.KEYWORD_TYPE);
        types.put("user_agent", FieldTypes.KEYWORD_TYPE);
        types.put("remote_log_name", FieldTypes.KEYWORD_TYPE);
        types.put("sequential_time_id", FieldTypes.INTEGER_TYPE);

        for (Map.Entry<String, Object> pair : data.getSource().entrySet()) {

            if (!types.containsKey(pair.getKey())) {
                throw new IllegalStateException(String.format("for the field '%s' does not exist a mapping field definition", pair.getKey()));
            }

            prepareMappingField(pair.getKey(), types.get(pair.getKey()));
        }
    }

    private void prepareMappingField(String field, String type) {

        if (EXISTING_FIELDS.contains(field)) {
            return;
        }

        elasticSearchService.prepareMappingField(field, type);

        EXISTING_FIELDS.add(field);
    }
}

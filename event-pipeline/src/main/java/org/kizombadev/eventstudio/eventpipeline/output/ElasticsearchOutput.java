package org.kizombadev.eventstudio.eventpipeline.output;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchProperties;
import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ElasticsearchOutput implements Output {

    private static final List<String> EXISTING_FIELDS = Collections.synchronizedList(new ArrayList());
    private static final String DEFAULT_DOC_TYPE = "_doc";
    private TransportClient transportClient;
    private ElasticsearchProperties elasticsearchProperties;

    @Autowired
    public ElasticsearchOutput(TransportClient transportClient, ElasticsearchProperties elasticsearchProperties) {
        this.transportClient = transportClient;
        this.elasticsearchProperties = elasticsearchProperties;
    }

    @PostConstruct
    public void init() {
        prepareIndex();
    }

    @Override
    public void write(List<EventEntry> logEntries) {
        logEntries.forEach(this::prepareMapping);
        String indexName = elasticsearchProperties.getIndexName();

        BulkRequestBuilder bulkRequest = transportClient.prepareBulk();

        for (EventEntry data : logEntries) {
            bulkRequest.add(transportClient.prepareIndex(indexName, DEFAULT_DOC_TYPE).setSource(data.getSource()));
        }

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            throw new IllegalStateException(bulkResponse.buildFailureMessage());
        }
    }

    private void prepareMapping(EventEntry data) {
        final String KEYWORD_TYPE = "keyword";
        final String DATE_TYPE = "date";
        final String IP_TYPE = "ip";
        final String INTEGER_TYPE = "integer";
        final String TEXT_TYPE = "text";
        final String BOOLEAN_TYPE = "boolean";

        //TODO extract to config file
        Map<String, String> types = new HashMap<>();
        types.put("source_id", KEYWORD_TYPE);
        types.put("type", KEYWORD_TYPE);
        types.put("timestamp", DATE_TYPE);
        types.put("origin", TEXT_TYPE);

        types.put("status", KEYWORD_TYPE);
        types.put("time", INTEGER_TYPE);
        types.put("ttl", INTEGER_TYPE);
        types.put("ip", IP_TYPE);
        types.put("bytes", INTEGER_TYPE);
        types.put(EventKeys.TIME_MANIPULATION, BOOLEAN_TYPE);

        types.put("length", INTEGER_TYPE);
        types.put("request_method", KEYWORD_TYPE);
        types.put("path", KEYWORD_TYPE);
        types.put("request_version", KEYWORD_TYPE);
        types.put("response_status", INTEGER_TYPE);
        types.put("host", KEYWORD_TYPE);
        types.put("referrer", KEYWORD_TYPE);
        types.put("user_agent", KEYWORD_TYPE);
        types.put("remote_log_name", KEYWORD_TYPE);
        types.put("sequential_time_id", INTEGER_TYPE);

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

        PutMappingResponse response = transportClient
                .admin()
                .indices()
                .preparePutMapping(elasticsearchProperties.getIndexName())
                .setType(DEFAULT_DOC_TYPE)
                .setSource("{\n" +
                        "  \"properties\": {\n" +
                        "    \"" + field + "\": {\n" +
                        "\t\t\"type\": \"" + type + "\"\n" +
                        "\t }\n" +
                        "  }\n" +
                        "}", XContentType.JSON).get();
        checkResponse(response, "prepare mapping failed");

        EXISTING_FIELDS.add(field);
    }


    private void prepareIndex() {
        GetIndexResponse response = transportClient.admin().indices().prepareGetIndex().get();

        boolean indexExist = Arrays.stream(response.getIndices()).anyMatch(x -> x.equals(elasticsearchProperties.getIndexName()));
        if (!indexExist) {
            createIndex();
        }
    }

    private void createIndex() {
        CreateIndexResponse response = transportClient.admin().indices().prepareCreate(elasticsearchProperties.getIndexName()).get();
        checkResponse(response, "the index creation failed");
    }

    private void checkResponse(AcknowledgedResponse response, String message) {
        if (!response.isAcknowledged()) {
            throw new IllegalStateException(message);
        }
    }
}

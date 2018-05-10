package org.kizombadev.pipeline.output;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.kizombadev.pipeline.Dataset;
import org.kizombadev.pipeline.properties.ElasticsearchProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class ElasticsearchOutput implements Output {

    private static final List<String> EXISTING_FIELDS = Collections.synchronizedList(new ArrayList());
    private static final String DEFAULT_DOC_TYPE = "ping";
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
    public void write(Dataset data) {
        prepareMapping(data);
        String indexName = elasticsearchProperties.getIndexName();
        IndexResponse indexResponse = transportClient.prepareIndex(indexName, DEFAULT_DOC_TYPE).setSource(data.getSource()).get();
        if (indexResponse.status() != RestStatus.CREATED) {
            throw new RuntimeException("Invalid response status: " + indexResponse.status());
        }
    }

    private void prepareMapping(Dataset data) {
        final String KEYWORD_TYPE = "keyword";
        final String DATE_TYPE = "date";
        final String IP_TYPE = "ip";
        final String INTEGER_TYPE = "integer";
        final String TEXT_TYPE = "text";

        Map<String, String> types = new HashMap<>();
        types.put("id", KEYWORD_TYPE);
        types.put("type", KEYWORD_TYPE);
        types.put("timestamp", DATE_TYPE);
        types.put("origin", TEXT_TYPE);

        types.put("status", KEYWORD_TYPE);
        types.put("time", INTEGER_TYPE);
        types.put("ttl", INTEGER_TYPE);
        types.put("ip", IP_TYPE);
        types.put("bytes", INTEGER_TYPE);

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

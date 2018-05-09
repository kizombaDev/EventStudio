package org.kizombadev.pipeline.output;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.rest.RestStatus;
import org.kizombadev.pipeline.Dataset;
import org.kizombadev.pipeline.properties.ElasticsearchProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ElasticsearchOutput implements Output {

    private TransportClient transportClient;
    private ElasticsearchProperties elasticsearchProperties;

    @Autowired
    public ElasticsearchOutput(TransportClient transportClient, ElasticsearchProperties elasticsearchProperties) {
        this.transportClient = transportClient;
        this.elasticsearchProperties = elasticsearchProperties;
    }

    @Override
    public void write(Dataset data) {
        prepareMapping(data);
        String indexName = elasticsearchProperties.getIndexName();
        IndexResponse indexResponse = transportClient.prepareIndex(indexName, data.getType()).setSource(data.getSource()).get();
        if (indexResponse.status() != RestStatus.CREATED) {
            throw new RuntimeException("Invalid response status: " + indexResponse.status());
        }
    }

    private void prepareMapping(Dataset data) {
        //TODO prepare the mapping
    }

    private String extractMappingType(Map<String, Object> json) {
        Object typeAsString = json.get("type");
        if (typeAsString instanceof String) {
            return (String) typeAsString;
        }
        throw new IllegalStateException("the json data contains no correct json property 'type' with a String.");
    }
}

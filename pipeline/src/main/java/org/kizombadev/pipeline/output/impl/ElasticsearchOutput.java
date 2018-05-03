package org.kizombadev.pipeline.output.impl;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.rest.RestStatus;
import org.kizombadev.pipeline.output.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ElasticsearchOutput implements Output {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TransportClient transportClient;

    @Autowired
    public ElasticsearchOutput(TransportClient transportClient) {
        this.transportClient = transportClient;
    }

    @Override
    public void write(Map<String, Object> json) {
        IndexResponse indexResponse = transportClient.prepareIndex("ping2", "ping2").setSource(json).get();
        if (indexResponse.status() != RestStatus.CREATED) {
            throw new RuntimeException("Invalid response status: " + indexResponse.status());
        }
    }
}

package org.kizombadev.pipeline.output.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.kizombadev.pipeline.PipelineProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@ConfigurationProperties("pipeline")
public class ElasticsearchConfiguration implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {

    private TransportClient transportClient;
    private final PipelineProperties pipelineProperties;

    @Autowired
    public ElasticsearchConfiguration(PipelineProperties pipelineProperties) {
        this.pipelineProperties = pipelineProperties;
    }

    @Override
    public void destroy() {
        if (transportClient != null) {
            transportClient.close();
        }
    }

    @Override
    public TransportClient getObject() {
        return transportClient;
    }

    @Override
    public Class<?> getObjectType() {
        return TransportClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        buildClient();
    }

    private void buildClient() throws UnknownHostException {
        transportClient = new PreBuiltTransportClient(getSettings());
        for (PipelineProperties.Nodes node : pipelineProperties.getNodes()) {
            transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(node.getIp()), node.getPort()));
        }
    }

    private Settings getSettings() {
        return Settings.builder().put("cluster.name", pipelineProperties.getClusterName()).build();
    }
}

package org.kizombadev.eventstudio.common.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
@Deprecated
public class ElasticsearchConfiguration implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {

    private ElasticsearchProperties elasticsearchProperties;
    private TransportClient transportClient;

    @Autowired
    public void setElasticsearchProperties(ElasticsearchProperties elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
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
        for (ElasticsearchProperties.Node node : elasticsearchProperties.getNodes()) {
            //TODO remove the + 100 hack if the TransportClient is unused
            transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(node.getIp()), node.getPort() + 100));
        }
    }

    private Settings getSettings() {
        return Settings.builder()
                .put("cluster.name", elasticsearchProperties.getClusterName())
                .build();
    }
}

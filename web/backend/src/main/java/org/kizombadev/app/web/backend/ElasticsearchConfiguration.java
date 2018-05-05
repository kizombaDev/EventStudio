package org.kizombadev.app.web.backend;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ElasticsearchConfiguration implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final BackendProperties backendProperties;
    private TransportClient transportClient;

    @Autowired
    public ElasticsearchConfiguration(BackendProperties backendProperties) {
        this.backendProperties = backendProperties;
        log.info("getClusterName: " + backendProperties.getClusterName());
        log.info("getNodes: " + backendProperties.getNodes());
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
        for (BackendProperties.Nodes node : backendProperties.getNodes()) {
            transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(node.getIp()), node.getPort()));
        }
    }

    private Settings getSettings() {
        return Settings.builder().put("cluster.name", backendProperties.getClusterName()).build();
    }
}

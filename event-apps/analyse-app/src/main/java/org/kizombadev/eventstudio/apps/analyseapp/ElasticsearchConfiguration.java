package org.kizombadev.eventstudio.apps.analyseapp;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("backend")
public class ElasticsearchConfiguration implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {

    private String clusterName;
    private List<ElasticsearchConfiguration.Nodes> nodes = new ArrayList<>();
    private TransportClient transportClient;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public List<ElasticsearchConfiguration.Nodes> getNodes() {
        return nodes;
    }

    public void setNodes(List<ElasticsearchConfiguration.Nodes> nodes) {
        this.nodes = nodes;
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
        for (ElasticsearchConfiguration.Nodes node : getNodes()) {
            transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(node.getIp()), node.getPort()));
        }
    }

    private Settings getSettings() {
        return Settings.builder().put("cluster.name", clusterName).build();
    }

    public static class Nodes {
        private String ip;
        private Integer port;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }
}

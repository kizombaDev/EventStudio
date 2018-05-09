package org.kizombadev.pipeline.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("pipeline.elasticsearch")
public class ElasticsearchProperties {

    private static final String PROPERTY_ROOT = "pipeline.elasticsearch.";

    private String clusterName;
    private List<Nodes> nodes = new ArrayList<>();
    private String indexName;

    public String getClusterName() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "clusterName", clusterName);
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "clusterName", clusterName);
        this.clusterName = clusterName;
    }

    public List<Nodes> getNodes() {
        return nodes;
    }

    public void setNodes(List<Nodes> nodes) {
        this.nodes = nodes;
    }

    public String getIndexName() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "indexName", indexName);
        return indexName;
    }

    public void setIndexName(String indexName) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "indexName", indexName);
        this.indexName = indexName;
    }

    public static class Nodes {
        private static final String PROPERTY_ROOT_NODES = PROPERTY_ROOT + "nodes.";
        private String ip;
        private Integer port;

        public String getIp() {
            PropertyHelper.validateNotEmpty(PROPERTY_ROOT_NODES + "ip", ip);
            return ip;
        }

        public void setIp(String ip) {
            PropertyHelper.logPropertyValue(PROPERTY_ROOT_NODES + "ip", ip);
            this.ip = ip;
        }

        public Integer getPort() {
            PropertyHelper.validateNotEmpty(PROPERTY_ROOT_NODES + "port", port);
            return port;
        }

        public void setPort(Integer port) {
            PropertyHelper.logPropertyValue(PROPERTY_ROOT_NODES + "port", port);
            this.port = port;
        }
    }
}

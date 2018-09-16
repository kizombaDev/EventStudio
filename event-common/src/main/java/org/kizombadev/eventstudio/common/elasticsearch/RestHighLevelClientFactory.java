package org.kizombadev.eventstudio.common.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class RestHighLevelClientFactory implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {

    private ElasticsearchProperties elasticsearchProperties;
    private RestHighLevelClient client;

    @Autowired
    public void setElasticsearchProperties(ElasticsearchProperties elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
    }

    @Override
    public void destroy() {
        if (client != null) {
            try {
                client.close();
                client = null;
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Override
    public RestHighLevelClient getObject() {
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        buildClient();
    }

    private void buildClient() {
        HttpHost[] hosts = elasticsearchProperties.getNodes().stream().map(node -> new HttpHost(node.getIp(), node.getPort(), "http")).toArray(HttpHost[]::new);
        client = new RestHighLevelClient(RestClient.builder(hosts));
    }
}

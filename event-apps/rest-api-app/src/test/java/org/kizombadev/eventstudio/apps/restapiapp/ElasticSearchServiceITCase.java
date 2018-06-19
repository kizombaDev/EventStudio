package org.kizombadev.eventstudio.apps.restapiapp;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration
public class ElasticSearchServiceITCase {

    @Autowired
    private TransportClient transportClient;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Before
    public void init() {
        transportClient.admin().indices().prepareCreate("test").get();
    }

    @Test
    @Ignore
    public void test() {
        List<Map<String, Object>> result = elasticSearchService.getElementsByFilter(new ArrayList<>(), 0, 1);
        assertThat(result).hasSize(1);
    }
}

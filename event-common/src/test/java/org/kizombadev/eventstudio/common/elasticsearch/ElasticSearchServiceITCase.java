package org.kizombadev.eventstudio.common.elasticsearch;

import org.assertj.core.api.Assertions;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.CommonTestApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CommonTestApp.class)
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
    public void test() {
        List<Map<String, Object>> result = elasticSearchService.getElementsByFilter(new ArrayList<>(), 0, 1);
        Assertions.assertThat(result).hasSize(1);
    }
}

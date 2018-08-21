package org.kizombadev.eventstudio.common.elasticsearch;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.CommonTestApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CommonTestApp.class)
@TestConfiguration
public class ElasticsearchPropertiesITCase {

    @Autowired
    private ElasticsearchProperties elasticsearchProperties;

    @Test
    public void testGetIndexName() {
        Assert.assertEquals("test", elasticsearchProperties.getIndexName());
    }

    @Test
    public void testClusterName() {
        Assert.assertEquals("elasticsearch", elasticsearchProperties.getClusterName());
    }

    @Test
    public void testNodes() {
        List<ElasticsearchProperties.Node> nodes = elasticsearchProperties.getNodes();
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals(Integer.valueOf(9300), nodes.get(0).getPort());
        Assert.assertEquals("localhost", nodes.get(0).getIp());
    }
}

package org.kizombadev.eventstudio.common.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.CommonTestApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CommonTestApp.class)
@TestConfiguration
public class RestHighLevelClientFactoryTest {

    @Autowired
    private RestHighLevelClientFactory factory;

    @Test
    public void destroy() {
        factory.afterPropertiesSet();
        factory.destroy();
        Assert.assertNull(factory.getObject());

        factory.destroy();
    }

    @Test
    public void getObject() {
        factory.afterPropertiesSet();
        Assert.assertNotNull(factory.getObject());
    }

    @Test
    public void getObjectType() {
        Assert.assertEquals(RestHighLevelClient.class, factory.getObjectType());
    }

    @Test
    public void isSingleton() {
        Assert.assertTrue(factory.isSingleton());
    }

    @Test
    public void afterPropertiesSet() {
        factory.afterPropertiesSet();
        Assert.assertNotNull(factory.getObject());
    }
}
package org.kizombadev.eventstudio.common.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
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
public class TransportClientFactoryTest {

    @Autowired
    private TransportClientFactory factory;

    @Test
    public void destroy() throws Exception {
        factory.afterPropertiesSet();
        factory.destroy();
        Assert.assertNull(factory.getObject());

        factory.destroy();
    }

    @Test
    public void getObject() throws Exception {
        factory.afterPropertiesSet();
        Assert.assertNotNull(factory.getObject());
    }

    @Test
    public void getObjectType() {
        Assert.assertEquals(TransportClient.class, factory.getObjectType());
    }

    @Test
    public void isSingleton() {
        Assert.assertTrue(factory.isSingleton());
    }

    @Test
    public void afterPropertiesSet() throws Exception {
        factory.afterPropertiesSet();
        Assert.assertNotNull(factory.getObject());
    }
}
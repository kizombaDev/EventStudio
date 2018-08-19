package org.kizombadev.eventstudio.clients.pingclient;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration
public class PingClientPropertiesITCase {

    @Autowired
    private PingClientProperties pingClientProperties;

    @Test
    public void test() {
        List<PingClientProperties.ClientConfig> clientConfigs =  pingClientProperties.getClients();
        Assert.assertEquals(4, clientConfigs.size());
        PingClientProperties.ClientConfig actualConfig = clientConfigs.get(2);
        Assert.assertEquals("PingGeneration", actualConfig.getName());
        Assert.assertEquals("fau", actualConfig.getId());
        Assert.assertEquals(1, actualConfig.getConfiguration().size());
        Assert.assertEquals("host", actualConfig.getConfiguration().get(0).getKey());
        Assert.assertEquals("www.fau.de", actualConfig.getConfiguration().get(0).getValue());
    }
}

package org.kizombadev.eventstudio.clients.pingclient.action;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.SystemUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.clients.pingclient.output.OutputService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
public class PingGenerationTest {

    private PingGeneration pingGeneration = new PingGeneration();

    @MockBean
    private OutputService outputService;

    @Test
    public void testRun() {
        Assume.assumeTrue(SystemUtils.IS_OS_WINDOWS);
        //Arrange
        Map<String, String> configuration = ImmutableMap.of("host", "127.0.0.1");

        //Act
        pingGeneration.init("localhost", outputService, configuration);
        pingGeneration.run();

        //Assert
        ArgumentCaptor<Map<String, Object>> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        Mockito.verify(outputService, Mockito.times(1)).handleSend(mapArgumentCaptor.capture());

        Map<String, Object> actualMap = mapArgumentCaptor.getValue();
        Assert.assertEquals("ping_localhost", actualMap.get("source_id"));
        Assert.assertEquals("ping", actualMap.get("type"));
        Assert.assertTrue(actualMap.containsKey("origin"));
        Assert.assertTrue(actualMap.containsKey("timestamp"));
    }

    @Test
    public void testInstanceCopy() {
        //Act
        PingClientAction pingClientAction = pingGeneration.instanceCopy();

        //Assert
        Assert.assertTrue(pingClientAction instanceof PingGeneration);
    }
}

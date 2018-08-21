package org.kizombadev.eventstudio.clients.pingclient.output;


import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.EventKeys;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
public class OutputServiceImplTest {

    @MockBean
    private ConsoleOutput consoleOutput;

    @MockBean
    private EventPipelineRestClient eventPipelineRestClient;

    private OutputServiceImpl outputServiceImpl = null;

    @Before
    public void init() {
        List<ClientOutput> outputs = new ArrayList<>();
        outputs.add(consoleOutput);
        outputs.add(eventPipelineRestClient);
        outputServiceImpl = new OutputServiceImpl(outputs);
    }

    @Test
    public void test() {
        //Arrange
        Map<EventKeys, Object> map = ImmutableMap.of(EventKeys.SOURCE_ID, "google", EventKeys.TYPE, "ping");

        //Act
        outputServiceImpl.handleSend(map);

        //Assert
        Mockito.verify(eventPipelineRestClient, Mockito.times(1)).send(map);
        Mockito.verify(consoleOutput, Mockito.times(1)).send(map);
    }
}

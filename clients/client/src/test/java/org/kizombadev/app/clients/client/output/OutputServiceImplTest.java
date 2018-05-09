package org.kizombadev.app.clients.client.output;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OutputServiceImplTest {

    @Autowired
    private OutputServiceImpl outputServiceImpl;

    @MockBean
    private ConsoleOutput consoleOutput;

    @MockBean
    private HttpOutput httpOutput;

    @Test
    public void test() {
        //Arrange
        Map<String, Object> map = ImmutableMap.of("id", "google", "type", "ping");

        //Act
        outputServiceImpl.handleSend(map);

        //Assert
        Mockito.verify(httpOutput, Mockito.times(1)).send(map);
        Mockito.verify(consoleOutput, Mockito.times(1)).send(map);
    }
}

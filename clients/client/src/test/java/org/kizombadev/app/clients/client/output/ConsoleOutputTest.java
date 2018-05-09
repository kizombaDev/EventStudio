package org.kizombadev.app.clients.client.output;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
public class ConsoleOutputTest {

    private ConsoleOutput consoleOutput = new ConsoleOutput();

    @Test
    public void test() {
        //Arrange
        Map<String, Object> map = ImmutableMap.of("id", "google", "type", "ping");

        //Act
        consoleOutput.send(map);

        //Assert
        //no exception is thrown
    }
}

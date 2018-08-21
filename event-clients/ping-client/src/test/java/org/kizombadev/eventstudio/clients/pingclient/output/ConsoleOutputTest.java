package org.kizombadev.eventstudio.clients.pingclient.output;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
public class ConsoleOutputTest {

    private ConsoleOutput consoleOutput = new ConsoleOutput();

    @Test
    public void test() {
        //Arrange
        Map<EventKeys, Object> map = ImmutableMap.of(EventKeys.SOURCE_ID, "google", EventKeys.TYPE, "ping");

        //Act
        consoleOutput.send(map);

        //Assert
        //no exception is thrown
    }
}

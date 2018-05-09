package org.kizombadev.app.clients.client.output;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConsoleOutputTest {

    @Autowired
    private ConsoleOutput consoleOutput;


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

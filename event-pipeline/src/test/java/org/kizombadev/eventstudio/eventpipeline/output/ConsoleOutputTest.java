package org.kizombadev.eventstudio.eventpipeline.output;

import org.junit.Before;
import org.junit.Test;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.eventpipeline.EventEntry;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ConsoleOutputTest {

    private ConsoleOutput underTest;
    private EventEntry eventEntry;

    @Before
    public void init() {
        underTest = new ConsoleOutput();

        Map<EventKeys, Object> source = new HashMap<EventKeys, Object>() {{
            put(EventKeys.SOURCE_ID, "abc");
            put(EventKeys.TYPE, "access_log");
            put(EventKeys.TIMESTAMP, LocalDateTime.of(2014, 1, 1, 1, 1).toString());
        }};
        eventEntry = new EventEntry(source);
    }

    @Test
    public void write() {
        //act
        underTest.write(eventEntry);

        //Assert
        //no exception is thrown
    }

    @Test
    public void writeAsCollection() {
        //act
        underTest.write(Collections.singletonList(eventEntry));

        //Assert
        //no exception is thrown
    }
}
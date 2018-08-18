package org.kizombadev.eventstudio.eventpipeline.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.kizombadev.eventstudio.eventpipeline.filter.FilterService;
import org.kizombadev.eventstudio.eventpipeline.output.Output;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class PipelineServiceImplTest {

    @Mock
    private Output output;

    @Mock
    private FilterService filterService;

    @Test
    public void testRun() {
        //Arrange
        PipelineService pipelineService = new PipelineServiceImpl(Arrays.asList(output), filterService);
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EventKeys.SOURCE_ID, "ping_google");
            put(EventKeys.TYPE, "ping");
        }};
        EventEntry eventEntry = new EventEntry(source);

        //Act
        pipelineService.run(Arrays.asList(eventEntry));

        //Assert
        Mockito.verify(output, Mockito.times(1)).write(Mockito.any());
        Mockito.verify(filterService, Mockito.times(1)).handle(eventEntry);
    }
}
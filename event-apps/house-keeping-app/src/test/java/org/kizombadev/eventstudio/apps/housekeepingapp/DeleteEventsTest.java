package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class DeleteEventsTest {

    @Mock
    private ElasticsearchService elasticsearchService;

    @Mock
    private Properties properties;

    private DeleteEvents underTest;

    @Before
    public void init() {
        underTest = new DeleteEvents(elasticsearchService, properties);
    }

    @Test
    public void testIndexIsTooBig() {
        //arrange
        final long maxIndexMbSize = 1024;
        List<Map<String, Object>> response = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("key", "2018-08-14");
        response.add(map);
        Mockito.when(elasticsearchService.getIndexSizeInMb()).thenReturn(maxIndexMbSize + 1);
        Mockito.when(properties.getMaxIndexMbSize()).thenReturn(maxIndexMbSize);
        Mockito.when(elasticsearchService.getDateDiagram(Mockito.any())).thenReturn(response);

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticsearchService, Mockito.times(1)).deleteEventsUntilDate(Mockito.eq(LocalDate.of(2018, 8, 14)));
    }

    @Test
    public void testStupidConfiguration() {
        //arrange
        final long maxIndexMbSize = 0;
        Mockito.when(elasticsearchService.getIndexSizeInMb()).thenReturn(maxIndexMbSize);
        Mockito.when(properties.getMaxIndexMbSize()).thenReturn(maxIndexMbSize);
        Mockito.when(elasticsearchService.getDateDiagram(Mockito.any())).thenReturn(new ArrayList<>());

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticsearchService, Mockito.never()).deleteEventsUntilDate(Mockito.any());
    }

    @Test
    public void testSmallIndexSize() {
        //arrange
        final long maxIndexMbSize = 1024;
        Mockito.when(elasticsearchService.getIndexSizeInMb()).thenReturn(maxIndexMbSize - 1);
        Mockito.when(properties.getMaxIndexMbSize()).thenReturn(maxIndexMbSize);

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticsearchService, Mockito.never()).deleteEventsUntilDate(Mockito.any());
    }


}
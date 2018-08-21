package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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
    public void testRun() {
        //arrange
        final int storageTime = 42;
        Mockito.when(properties.getStorageTime()).thenReturn(storageTime);

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticsearchService, Mockito.times(1)).deleteEvents(Mockito.eq(storageTime));
    }
}
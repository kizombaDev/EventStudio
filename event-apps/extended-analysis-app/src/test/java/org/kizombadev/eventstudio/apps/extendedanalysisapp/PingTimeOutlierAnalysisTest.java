package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class PingTimeOutlierAnalysisTest {

    private static final EventKeys TIME_OUTLIER_FIELD = EventKeys.forValue("time_outlier");

    @Mock
    private ElasticsearchService elasticSearchService;

    @Mock
    private Properties properties;

    @Captor
    private ArgumentCaptor<List<FilterCriteriaDto>> filterCaptor;

    private PingTimeOutlierAnalysis underTest;

    @Before
    public void init() {
        underTest = new PingTimeOutlierAnalysis(elasticSearchService, properties);

        Mockito.when(properties.getPercentile()).thenReturn(25);
        Mockito.when(properties.getInterquartileFactor()).thenReturn(1.5);
        Mockito.when(properties.getMaxHistoricalEvents()).thenReturn(50);
    }

    @Test
    public void prepareReferenceFieldMapping() {
        //Arrange

        //act
        underTest.prepareReferenceFieldMapping();

        //assert
        Mockito.verify(elasticSearchService, Mockito.times(1)).prepareMappingField(TIME_OUTLIER_FIELD, MappingType.BOOLEAN_TYPE);
    }

    @Test
    public void runWithNoEvents() {
        //arrange
        Mockito.when(elasticSearchService.getElementsByFilter(Mockito.any(), Mockito.eq(0), Mockito.eq(100))).thenReturn(new ArrayList<>());

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticSearchService, Mockito.never()).updateField(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(elasticSearchService, Mockito.times(1)).getElementsByFilter(filterCaptor.capture(), Mockito.eq(0), Mockito.eq(100));

        Assert.assertEquals(3, filterCaptor.getValue().size());
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(0));
        Assert.assertEquals(new FilterCriteriaDto(TIME_OUTLIER_FIELD, "", FilterType.PRIMARY, FilterOperation.NOT_EXIST), filterCaptor.getValue().get(1));
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.TIME, "", FilterType.PRIMARY, FilterOperation.EXIST), filterCaptor.getValue().get(2));
    }

    @Test
    public void run() {
        //arrange
        final String sourceId = "fau_ping";
        final LocalDateTime timestamp = LocalDateTime.now();

        Map<String, Object> currentPing = new HashMap<>();
        currentPing.put(EventKeys.SOURCE_ID.getValue(), sourceId);
        currentPing.put(EventKeys.TIMESTAMP.getValue(), timestamp);
        currentPing.put(EventKeys.TIME.getValue(), 11);

        Map<String, Object> historicalPingOne = new HashMap<>();
        historicalPingOne.put(EventKeys.TIME.getValue(), 10);

        Map<String, Object> historicalPingTwo = new HashMap<>();
        historicalPingTwo.put(EventKeys.TIME.getValue(), 12);

        Mockito.when(elasticSearchService.getElementsByFilter(Mockito.any(), Mockito.eq(0), Mockito.eq(100))).thenReturn(Collections.singletonList(currentPing));
        Mockito.when(elasticSearchService.getElementsByFilter(Mockito.any(), Mockito.eq(0), Mockito.eq(50))).thenReturn(Arrays.asList(historicalPingOne, historicalPingTwo));

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticSearchService, Mockito.times(1)).updateField(filterCaptor.capture(), Mockito.eq(TIME_OUTLIER_FIELD), Mockito.eq("false"));
        Assert.assertEquals(3, filterCaptor.getValue().size());
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(0));
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.SOURCE_ID, sourceId, FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(1));
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.TIMESTAMP, timestamp.toString(), FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(2));
    }
}
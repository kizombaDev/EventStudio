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

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class ReferenceAnalysisTest {

    private static final String REFERENCE_FIELD = "link";

    @Mock
    private ElasticsearchService elasticSearchService;

    @Mock
    private Properties properties;

    @Captor
    private ArgumentCaptor<List<FilterCriteriaDto>> filterCaptor;

    private ReferenceAnalysis underTest;

    @Before
    public void init() {
        underTest = new ReferenceAnalysis(elasticSearchService, properties);

        Mockito.when(properties.getIndicatorField()).thenReturn(EventKeys.IP);
        Mockito.when(properties.getReferenceField()).thenReturn(REFERENCE_FIELD);
        Mockito.when(properties.getEventType()).thenReturn("access_log");
    }

    @Test
    public void prepareReferenceFieldMapping() {
        //Arrange

        //act
        underTest.prepareReferenceFieldMapping();

        //assert
        Mockito.verify(elasticSearchService, Mockito.times(1)).prepareMappingField(REFERENCE_FIELD, MappingType.KEYWORD_TYPE);
    }

    @Test
    public void runWithNoEvents() {
        //arrange
        Mockito.when(elasticSearchService.getTermDiagram(Mockito.any(), Mockito.eq(EventKeys.IP), Mockito.eq(100))).thenReturn(new ArrayList<>());

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticSearchService, Mockito.never()).updateField(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(elasticSearchService, Mockito.times(1)).getTermDiagram(filterCaptor.capture(), Mockito.eq(EventKeys.IP), Mockito.eq(100));

        Assert.assertEquals(2, filterCaptor.getValue().size());
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.TYPE, "access_log", FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(0));
        Assert.assertEquals(new FilterCriteriaDto(REFERENCE_FIELD, "", FilterType.PRIMARY, FilterOperation.NOT_EXIST), filterCaptor.getValue().get(1));
    }

    @Test
    public void runWithGeneratedUUID() {
        //arrange
        final String ip = "192.068.0.1";
        Map<String, Object> map = new HashMap<>();
        map.put("key", ip);

        Mockito.when(elasticSearchService.getTermDiagram(Mockito.any(), Mockito.eq(EventKeys.IP), Mockito.eq(100))).thenReturn(Collections.singletonList(map), new ArrayList<>());

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticSearchService, Mockito.times(1)).updateField(filterCaptor.capture(), Mockito.eq(REFERENCE_FIELD), Mockito.any());
        Assert.assertEquals(3, filterCaptor.getValue().size());
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.TYPE, "access_log", FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(0));
        Assert.assertEquals(new FilterCriteriaDto(REFERENCE_FIELD, "", FilterType.PRIMARY, FilterOperation.NOT_EXIST), filterCaptor.getValue().get(1));
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.IP, ip, FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(2));
    }

    @Test
    public void runWithFindUUID() {
        //arrange
        final String existingUUID = "161ef640-5ab6-49ea-a08e-653aacb150cc";
        final String ip = "192.068.0.1";

        Map<String, Object> map = new HashMap<>();
        map.put("key", ip);

        Mockito.when(elasticSearchService.getTermDiagram(Mockito.any(), Mockito.eq(EventKeys.IP), Mockito.eq(100))).thenReturn(Collections.singletonList(map), new ArrayList<>());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("link", existingUUID);
        Mockito.when(elasticSearchService.getElementsByFilter(Mockito.any(), Mockito.eq(0), Mockito.eq(1))).thenReturn(Collections.singletonList(resultMap));

        //act
        underTest.run();

        //assert
        Mockito.verify(elasticSearchService, Mockito.times(1)).updateField(filterCaptor.capture(), Mockito.eq(REFERENCE_FIELD), Mockito.eq(existingUUID));

        Assert.assertEquals(3, filterCaptor.getValue().size());
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.TYPE, "access_log", FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(0));
        Assert.assertEquals(new FilterCriteriaDto(REFERENCE_FIELD, "", FilterType.PRIMARY, FilterOperation.NOT_EXIST), filterCaptor.getValue().get(1));
        Assert.assertEquals(new FilterCriteriaDto(EventKeys.IP, ip, FilterType.PRIMARY, FilterOperation.EQUALS), filterCaptor.getValue().get(2));
    }
}
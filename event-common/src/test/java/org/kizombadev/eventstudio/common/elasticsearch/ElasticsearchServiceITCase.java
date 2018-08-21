package org.kizombadev.eventstudio.common.elasticsearch;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.CommonTestApp;
import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CommonTestApp.class)
@TestConfiguration
public class ElasticsearchServiceITCase {

    private static final int TIMEOUT = 1;

    @Autowired
    private ElasticsearchService elasticSearchService;

    @Before
    public void init() {
        elasticSearchService.prepareIndex();
        elasticSearchService.prepareMappingField(EventKeys.TIMESTAMP, FieldTypes.DATE_TYPE);
        elasticSearchService.prepareMappingField(EventKeys.SOURCE_ID, FieldTypes.KEYWORD_TYPE);
        elasticSearchService.prepareMappingField(EventKeys.TYPE, FieldTypes.KEYWORD_TYPE);
        elasticSearchService.prepareMappingField(EventKeys.SEQUENTIAL_TIME_ID, FieldTypes.INTEGER_TYPE);
        elasticSearchService.deleteEvents(0);

        sleep(TIMEOUT);
        final Map<String, Object> pingOne = new HashMap<>();
        pingOne.put(EventKeys.TIMESTAMP, LocalDateTime.of(2014,8,14,12,12,12).toString());
        pingOne.put(EventKeys.SOURCE_ID, "fau_ping");
        pingOne.put(EventKeys.TYPE, "ping");
        pingOne.put(EventKeys.SEQUENTIAL_TIME_ID, 1);
        final Map<String, Object> pingTwo = new HashMap<>();
        pingTwo.put(EventKeys.TIMESTAMP, LocalDateTime.of(2014,8,15,12,12,12).toString());
        pingTwo.put(EventKeys.SOURCE_ID, "facebook_ping");
        pingTwo.put(EventKeys.TYPE, "ping");
        pingTwo.put(EventKeys.SEQUENTIAL_TIME_ID, 2);

        elasticSearchService.bulkInsert(Arrays.asList(pingOne, pingTwo));
        sleep(TIMEOUT);
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetElementsByFilter() {
        //act
        List<Map<String, Object>> result = elasticSearchService.getElementsByFilter(new ArrayList<>(), 0, 1);

        //assert
        Assert.assertEquals(1, result.size());
        Map<String, Object> data = result.get(0);
        Assert.assertEquals("facebook_ping", data.get(EventKeys.SOURCE_ID) );
        Assert.assertEquals("ping", data.get(EventKeys.TYPE) );
    }

    @Test
    public void testGetElementsByFilterWithCriteriaFilter() {
        //arrange
        FilterCriteriaDto dto = new FilterCriteriaDto(EventKeys.SOURCE_ID, "fau_ping", FilterType.PRIMARY, FilterOperation.EQUALS);

        //act
        List<Map<String, Object>> result = elasticSearchService.getElementsByFilter(Collections.singletonList(dto), 0, 1);

        //assert
        Assert.assertEquals(1, result.size());
        Map<String, Object> data = result.get(0);
        Assert.assertEquals("fau_ping", data.get(EventKeys.SOURCE_ID) );
        Assert.assertEquals("ping", data.get(EventKeys.TYPE) );
    }

    @Test
    public void testGetFieldStructure() {
        //act
        List<Map<String, String>> fieldStructure = elasticSearchService.getFieldStructure();

        //assert
        Assert.assertEquals(4, fieldStructure.size());
        Map<String, String> test = fieldStructure.get(0);
        Assert.assertEquals(EventKeys.SEQUENTIAL_TIME_ID, test.get("field"));
        Assert.assertEquals(FieldTypes.INTEGER_TYPE, test.get("type"));
    }

    @Test
    public void testGetMaxValue() {
        //arrange
        FilterCriteriaDto dto = new FilterCriteriaDto(EventKeys.SOURCE_ID, "fau_ping", FilterType.PRIMARY, FilterOperation.EQUALS);

        //act
        double maxValue = elasticSearchService.getMaxValue(Collections.singletonList(dto), EventKeys.SEQUENTIAL_TIME_ID);

        //assert
        Assert.assertEquals(1.0, maxValue, 0.00001);
    }

    @Test
    public void testUpdateField() {
        //arrange
        FilterCriteriaDto dto = new FilterCriteriaDto(EventKeys.SOURCE_ID, "fau_ping", FilterType.PRIMARY, FilterOperation.EQUALS);

        //act
        elasticSearchService.updateField(Collections.singletonList(dto), EventKeys.SEQUENTIAL_TIME_ID, "42");

        sleep(TIMEOUT);
        Assert.assertEquals(42, elasticSearchService.getMaxValue(Collections.singletonList(dto), EventKeys.SEQUENTIAL_TIME_ID), 1.000);
    }
}

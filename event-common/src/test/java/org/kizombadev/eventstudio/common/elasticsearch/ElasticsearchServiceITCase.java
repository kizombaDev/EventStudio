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

    //todo make this test faster

    private static final int TIMEOUT = 5;

    @Autowired
    private ElasticsearchService elasticSearchService;

    @Before
    public void init() {
        elasticSearchService.prepareIndex();
        elasticSearchService.prepareMappingField(EventKeys.TIMESTAMP, MappingType.DATE_TYPE);
        elasticSearchService.prepareMappingField(EventKeys.SOURCE_ID, MappingType.KEYWORD_TYPE);
        elasticSearchService.prepareMappingField(EventKeys.TYPE, MappingType.KEYWORD_TYPE);
        elasticSearchService.prepareMappingField(EventKeys.SEQUENTIAL_TIME_ID, MappingType.INTEGER_TYPE);
        elasticSearchService.deleteEvents(0);
        sleep(TIMEOUT);

        final Map<EventKeys, Object> pingOne = new HashMap<>();
        pingOne.put(EventKeys.TIMESTAMP, LocalDateTime.of(2014, 8, 14, 12, 12, 12).toString());
        pingOne.put(EventKeys.SOURCE_ID, "fau_ping");
        pingOne.put(EventKeys.TYPE, "ping");
        pingOne.put(EventKeys.SEQUENTIAL_TIME_ID, 1);

        final Map<EventKeys, Object> pingTwo = new HashMap<>();
        pingTwo.put(EventKeys.TIMESTAMP, LocalDateTime.of(2014, 8, 15, 12, 12, 12).toString());
        pingTwo.put(EventKeys.SOURCE_ID, "facebook_ping");
        pingTwo.put(EventKeys.TYPE, "ping");
        pingTwo.put(EventKeys.SEQUENTIAL_TIME_ID, 2);

        final Map<EventKeys, Object> accessLog = new HashMap<>();
        accessLog.put(EventKeys.TIMESTAMP, LocalDateTime.of(2014, 9, 15, 12, 12, 12).toString());
        accessLog.put(EventKeys.SOURCE_ID, "fcs");
        accessLog.put(EventKeys.TYPE, "access_log");

        elasticSearchService.bulkInsert(Arrays.asList(pingOne, pingTwo, accessLog));
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
        Assert.assertEquals("fcs", data.get(EventKeys.SOURCE_ID.getValue()));
        Assert.assertEquals("access_log", data.get(EventKeys.TYPE.getValue()));
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
        Assert.assertEquals("fau_ping", data.get(EventKeys.SOURCE_ID.getValue()));
        Assert.assertEquals("ping", data.get(EventKeys.TYPE.getValue()));
    }

    @Test
    public void testGetFieldStructure() {
        //act
        List<Map<String, String>> fieldStructure = elasticSearchService.getFieldStructure();

        //assert
        Assert.assertEquals(4, fieldStructure.size());
        Map<String, String> test = fieldStructure.get(0);
        Assert.assertEquals(EventKeys.SEQUENTIAL_TIME_ID.getValue(), test.get("field"));
        Assert.assertEquals(MappingType.INTEGER_TYPE.getValue(), test.get("type"));
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

        //assert
        sleep(TIMEOUT);
        Assert.assertEquals(42, elasticSearchService.getMaxValue(Collections.singletonList(dto), EventKeys.SEQUENTIAL_TIME_ID), 1.000);
    }

    @Test
    public void testGetDateHistogram() {
        //arrange
        FilterCriteriaDto primaryDto = new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS);
        FilterCriteriaDto secondaryDto = new FilterCriteriaDto(EventKeys.SEQUENTIAL_TIME_ID, "2", FilterType.SECONDARY, FilterOperation.GREATER_THEN_OR_EQUAL);
        final String primaryCount = "primary_count";
        final String secondaryCount = "secondary_count";
        final String key = "key";

        //act
        List<Map<String, Object>> dateHistogram = elasticSearchService.getDateHistogram(Arrays.asList(primaryDto, secondaryDto));

        //assert
        Assert.assertEquals(2, dateHistogram.size());
        Assert.assertEquals("14-08-2014", dateHistogram.get(0).get(key));
        Assert.assertEquals(1L, dateHistogram.get(0).get(primaryCount));
        Assert.assertEquals(0L, dateHistogram.get(0).get(secondaryCount));
        Assert.assertEquals("15-08-2014", dateHistogram.get(1).get(key));
        Assert.assertEquals(1L, dateHistogram.get(1).get(primaryCount));
        Assert.assertEquals(1L, dateHistogram.get(1).get(secondaryCount));
    }

    @Test
    public void testGetTermDiagram() {
        //arrange
        FilterCriteriaDto dto = new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS);
        final String count = "count";
        final String key = "key";

        //act
        List<Map<String, Object>> termDiagram = elasticSearchService.getTermDiagram(Collections.singletonList(dto), EventKeys.SEQUENTIAL_TIME_ID, 10);

        //assert
        Assert.assertEquals(2, termDiagram.size());
        Assert.assertEquals("1", termDiagram.get(0).get(key));
        Assert.assertEquals(1L, termDiagram.get(0).get(count));
        Assert.assertEquals("2", termDiagram.get(1).get(key));
        Assert.assertEquals(1L, termDiagram.get(1).get(count));
    }

}

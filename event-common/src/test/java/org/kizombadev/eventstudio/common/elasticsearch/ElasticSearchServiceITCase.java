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
public class ElasticSearchServiceITCase {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Before
    public void init() {
        elasticSearchService.prepareIndex();
        elasticSearchService.prepareMappingField(EventKeys.TIMESTAMP, FieldTypes.DATE_TYPE);
        elasticSearchService.prepareMappingField(EventKeys.SOURCE_ID, FieldTypes.KEYWORD_TYPE);
        elasticSearchService.prepareMappingField(EventKeys.TYPE, FieldTypes.KEYWORD_TYPE);
        elasticSearchService.deleteEvents(0);

        final Map<String, Object> pingOne = new HashMap<>();
        pingOne.put(EventKeys.TIMESTAMP, LocalDateTime.of(2014,8,14,12,12,12).toString());
        pingOne.put(EventKeys.SOURCE_ID, "fau_ping");
        pingOne.put(EventKeys.TYPE, "ping");
        final Map<String, Object> pingTwo = new HashMap<>();
        pingTwo.put(EventKeys.TIMESTAMP, LocalDateTime.of(2014,8,15,12,12,12).toString());
        pingTwo.put(EventKeys.SOURCE_ID, "facebook_ping");
        pingTwo.put(EventKeys.TYPE, "ping");

        elasticSearchService.bulkInsert(Arrays.asList(pingOne, pingTwo));
    }

    @Test
    public void testGetElementsByFilter() {
        List<Map<String, Object>> result = elasticSearchService.getElementsByFilter(new ArrayList<>(), 0, 1);
        Assert.assertEquals(1, result.size());
        Map<String, Object> data = result.get(0);
        Assert.assertEquals("facebook_ping", data.get(EventKeys.SOURCE_ID) );
        Assert.assertEquals("ping", data.get(EventKeys.TYPE) );
    }

    @Test
    public void testGetElementsByFilterWithCriteriaFilter() {
        FilterCriteriaDto dto = new FilterCriteriaDto();
        dto.setField(EventKeys.SOURCE_ID);
        dto.setOperator(FilterOperation.EQUALS);
        dto.setValue("fau_ping");
        dto.setType(FilterType.PRIMARY);

        List<Map<String, Object>> result = elasticSearchService.getElementsByFilter(Collections.singletonList(dto), 0, 1);
        Assert.assertEquals(1, result.size());
        Map<String, Object> data = result.get(0);
        Assert.assertEquals("fau_ping", data.get(EventKeys.SOURCE_ID) );
        Assert.assertEquals("ping", data.get(EventKeys.TYPE) );
    }
}

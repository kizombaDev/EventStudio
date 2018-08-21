package org.kizombadev.eventstudio.eventpipeline.output;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.kizombadev.eventstudio.common.elasticsearch.FilterCriteriaDto;
import org.kizombadev.eventstudio.common.elasticsearch.FilterOperation;
import org.kizombadev.eventstudio.common.elasticsearch.FilterType;
import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration
public class ElasticsearchOutputITCase {

    @Autowired
    private ElasticsearchOutput elasticsearchOutput;

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Test
    public void test() {
        //arrange
        Map<EventKeys, Object> source = new HashMap<EventKeys, Object>() {{
            put(EventKeys.SOURCE_ID, "foo");
            put(EventKeys.TYPE, "ping");
            put(EventKeys.TIMESTAMP, LocalDateTime.of(2014, 1, 1, 1, 1).toString());
            put(EventKeys.BYTES, "32");
        }};
        EventEntry eventEntry = new EventEntry(source);

        //act
        elasticsearchOutput.write(Collections.singletonList(eventEntry));

        //assert

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FilterCriteriaDto dto = new FilterCriteriaDto(EventKeys.SOURCE_ID, "foo", FilterType.PRIMARY, FilterOperation.EQUALS);
        List<Map<String, Object>> result = elasticsearchService.getElementsByFilter(Collections.singletonList(dto), 0, 1);
        Assert.assertEquals(1, result.size());
        Map<String, Object> data = result.get(0);
        Assert.assertEquals("foo", data.get(EventKeys.SOURCE_ID));
        Assert.assertEquals("ping", data.get(EventKeys.TYPE));
    }
}

package org.kizombadev.eventstudio.eventpipeline.filter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TimestampConverterFilterTest {

    private TimestampConverterFilter filter;

    @Before
    public void setup() {
        filter = new TimestampConverterFilter();
    }

    @Test
    public void testWithEnglishLocal() {
        Map<String, String> configuration = new HashMap<String, String>() {{
            put("pattern", "dd/MMM/yyyy:kk:mm:ss Z");
            put("lang", "en");
        }};
        filter.init(configuration);
        assertTimestampConversion("2018-05-13T20:08:03", "13/May/2018:20:08:03 +0200");
    }

    @Test
    public void testWithGermanLocal() {
        Map<String, String> configuration = new HashMap<String, String>() {{
            put("pattern", "dd/MMM/yyyy:kk:mm:ss Z");
            put("lang", "de");
        }};
        filter.init(configuration);
        assertTimestampConversion("2018-05-13T20:08:03", "13/Mai/2018:20:08:03 +0200");
    }

    @Test
    public void testInstanceCopy() {
        assertThat(filter.instanceCopy()).isExactlyInstanceOf(TimestampConverterFilter.class);
    }


    private void assertTimestampConversion(String expected, String from) {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put("timestamp", from);
        }};
        filter.handle(json);
        Assert.assertEquals(expected, json.get("timestamp"));
    }

}
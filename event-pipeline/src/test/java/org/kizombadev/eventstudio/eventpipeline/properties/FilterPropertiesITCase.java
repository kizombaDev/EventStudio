package org.kizombadev.eventstudio.eventpipeline.properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConfiguration
public class FilterPropertiesITCase {

    @Autowired
    private FilterProperties filterProperties;

    @Test
    public void test() {
        List<FilterProperties.FilterConfig> filterConfigs = filterProperties.getFilter();
        Assert.assertEquals(8, filterConfigs.size());
        FilterProperties.FilterConfig actualFilter = filterConfigs.get(4);
        Assert.assertEquals("TimestampConverterFilter", actualFilter.getName());
        Assert.assertEquals("access_log", actualFilter.getType());
        Assert.assertEquals(2, actualFilter.getConfiguration().size());
        Assert.assertEquals("lang", actualFilter.getConfiguration().get(1).getKey());
        Assert.assertEquals("en", actualFilter.getConfiguration().get(1).getValue());
    }
}

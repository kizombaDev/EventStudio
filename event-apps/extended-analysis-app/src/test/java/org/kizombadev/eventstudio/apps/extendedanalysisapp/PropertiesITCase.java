package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestConfiguration
public class PropertiesITCase {

    @Autowired
    private Properties properties;

    @Test
    public void testGetReferenceField() {
        Assert.assertEquals("link", properties.getReferenceField());
    }

    @Test
    public void testGetIndicatorField() {
        Assert.assertEquals("ip", properties.getIndicatorField());
    }

    @Test
    public void testGetEventType() {
        Assert.assertEquals("access_log", properties.getEventType());
    }
}

package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestConfiguration
public class PropertiesITCase {

    @Autowired
    private Properties properties;


    @Test
    public void testGetInterquartileFactor() {
        Assert.assertEquals(1.5, properties.getInterquartileFactor(), 0.00001);
    }

    @Test
    public void testGetMaxHistoricalEvents() {
        Assert.assertEquals(100, properties.getMaxHistoricalEvents());
    }

    @Test
    public void testGetPercentile() {
        Assert.assertEquals(10, properties.getPercentile());
    }
}

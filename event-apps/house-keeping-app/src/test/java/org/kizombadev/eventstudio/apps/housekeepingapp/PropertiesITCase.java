package org.kizombadev.eventstudio.apps.housekeepingapp;

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
public class PropertiesITCase {

    @Autowired
    private Properties properties;

    @Test
    public void testGetStorageTime() {
        Assert.assertEquals(30, properties.getStorageTime());
    }
}

package org.kizombadev.eventstudio.common.elasticsearch;

import org.junit.Assert;
import org.junit.Test;

import java.rmi.server.ExportException;

import static org.junit.Assert.*;

public class FilterTypeTest {

    @Test
    public void testForValue() {
        Assert.assertEquals(FilterType.PRIMARY, FilterType.forValue("primary"));
    }

    @Test(expected =  IllegalStateException.class)
    public void testForValueWithInvalidType() {
       FilterType.forValue("foo");
    }

    @Test
    public void testToString() {
        Assert.assertEquals("primary", FilterType.PRIMARY.toString());
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals("primary", FilterType.PRIMARY.getValue());
    }
}
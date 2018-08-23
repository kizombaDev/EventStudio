package org.kizombadev.eventstudio.common.elasticsearch;

import org.junit.Assert;
import org.junit.Test;

public class FilterOperationTest {

    @Test
    public void testForValue() {
        Assert.assertEquals(FilterOperation.EQUALS, FilterOperation.forValue("equals"));
    }

    @Test(expected = IllegalStateException.class)
    public void testForValueWithInvalidType() {
        FilterOperation.forValue("foo");
    }

    @Test
    public void testToString() {
        Assert.assertEquals("equals", FilterOperation.EQUALS.toString());
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals("equals", FilterOperation.EQUALS.getValue());
    }
}
package org.kizombadev.eventstudio.common.elasticsearch;

import org.junit.Assert;
import org.junit.Test;

public class MappingTypeTest {

    @Test
    public void testForValue() {
        Assert.assertEquals(MappingType.KEYWORD_TYPE, MappingType.forValue("keyword"));
    }

    @Test(expected = IllegalStateException.class)
    public void testForValueWithInvalidType() {
        MappingType.forValue("foo");
    }

    @Test
    public void testToString() {
        Assert.assertEquals("keyword", MappingType.KEYWORD_TYPE.toString());
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals("keyword", MappingType.KEYWORD_TYPE.getValue());
    }
}
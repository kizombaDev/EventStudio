package org.kizombadev.eventstudio.common;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventKeysTest {

    @Test
    public void testForValue() {
        EventKeys type = EventKeys.TYPE;
        EventKeys actual = EventKeys.forValue(EventKeys.TYPE.getValue());

        Assert.assertEquals(type, actual);
    }

    @Test
    public void testGetValue() {
        final String key = "foo";
        Assert.assertEquals(key, EventKeys.forValue(key).getValue());
    }

    @Test
    public void testToString() {
        final String key = "foo";
        Assert.assertEquals("foo", EventKeys.forValue(key).toString());
    }
}
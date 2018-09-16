package org.kizombadev.eventstudio.eventpipeline;

import org.junit.Assert;
import org.junit.Test;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.MappingType;

public class FieldMappingTest {

    @Test
    public void getTypeOfField() {
        Assert.assertEquals(MappingType.KEYWORD_TYPE, FieldMapping.getTypeOfField(EventKeys.SOURCE_ID));
        Assert.assertEquals(MappingType.DATE_TYPE, FieldMapping.getTypeOfField(EventKeys.TIMESTAMP));
    }

    @Test
    public void isFieldOfType() {
        Assert.assertTrue(FieldMapping.isFieldOfType(EventKeys.SOURCE_ID, MappingType.KEYWORD_TYPE));
        Assert.assertFalse(FieldMapping.isFieldOfType(EventKeys.SOURCE_ID, MappingType.DATE_TYPE));
        Assert.assertFalse(FieldMapping.isFieldOfType(EventKeys.forValue("Foo"), MappingType.DATE_TYPE));
    }

    @Test
    public void contains() {
        Assert.assertTrue(FieldMapping.contains(EventKeys.SOURCE_ID));
        Assert.assertFalse(FieldMapping.contains(EventKeys.forValue("Foo")));
    }
}
package org.kizombadev.pipeline.properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PropertyHelperTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testLogPropertyValue() {
        PropertyHelper.logPropertyValue("name", "value");
    }

    @Test
    public void testValidateNotEmpty() {
        PropertyHelper.validateNotEmpty("name", "value");
    }

    @Test
    public void testValidateNotEmptyWithNull() {
        thrown.expectMessage("The property name has no value");
        thrown.expect(IllegalStateException.class);

        PropertyHelper.validateNotEmpty("name", null);
    }

    @Test
    public void testValidateNotEmptyWithEmptyString() {
        thrown.expectMessage("The property name has no value");
        thrown.expect(IllegalStateException.class);

        PropertyHelper.validateNotEmpty("name", "");
    }

}
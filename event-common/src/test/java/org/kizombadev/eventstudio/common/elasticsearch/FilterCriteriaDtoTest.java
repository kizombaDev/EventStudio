package org.kizombadev.eventstudio.common.elasticsearch;

import org.junit.Assert;
import org.junit.Test;
import org.kizombadev.eventstudio.common.EventKeys;

public class FilterCriteriaDtoTest {

    private final FilterCriteriaDto dto = new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS);


    @Test
    public void testToString() {
        Assert.assertEquals("FilterCriteriaDto{field=type, value=ping, type=primary, operator=equals}", dto.toString());
    }

    @Test
    public void testEquals() {
        FilterCriteriaDto one = new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS);
        FilterCriteriaDto two = new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS);

        //act & assert
        Assert.assertEquals(one, two);
    }

    @Test
    public void testHashCode() {
        FilterCriteriaDto one = new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS);
        FilterCriteriaDto two = new FilterCriteriaDto(EventKeys.TYPE, "ping", FilterType.PRIMARY, FilterOperation.EQUALS);

        //act & assert
        Assert.assertEquals(one.hashCode(), two.hashCode());
    }
}
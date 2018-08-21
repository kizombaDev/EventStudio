package org.kizombadev.eventstudio.eventpipeline.filter;

import org.junit.Test;
import org.kizombadev.eventstudio.common.EventKeys;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AccessLogCleanerFilterTest {

    private final Filter filter = new AccessLogCleanerFilter();

    @Test
    public void testFixIPNormalization() {
        Map<EventKeys, Object> json = new HashMap<EventKeys, Object>() {{
            put(EventKeys.IP, "78.78.4.x");
        }};
        filter.handle(json);
        assertThat(json.get(EventKeys.IP).toString()).isEqualTo("78.78.4.0");
    }

    @Test
    public void testCleanEmptyValues() {
        Map<EventKeys, Object> json = new HashMap<EventKeys, Object>() {{
            put(EventKeys.BYTES, " - ");
        }};
        filter.handle(json);
        assertThat(json.get(EventKeys.BYTES).toString()).isEqualTo("");
    }

    @Test
    public void testInstanceCopy() {
        assertThat(filter.instanceCopy()).isExactlyInstanceOf(AccessLogCleanerFilter.class);
    }

}
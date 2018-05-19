package org.kizombadev.pipeline.filter;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class AccessLogCleanerFilterTest {

    private final Filter filter = new AccessLogCleanerFilter();

    @Test
    public void testFixIPNormalization() {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put("ip", "78.78.4.x");
        }};
        filter.handle(json);
        assertThat(json.get("ip").toString()).isEqualTo("78.78.4.0");
    }

    @Test
    public void testCleanEmptyValues() {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put("status", " - ");
        }};
        filter.handle(json);
        assertThat(json.get("status").toString()).isEqualTo("");
    }

    @Test
    public void testInstanceCopy() {
        assertThat(filter.instanceCopy()).isExactlyInstanceOf(AccessLogCleanerFilter.class);
    }

}
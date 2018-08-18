package org.kizombadev.eventstudio.eventpipeline.filter;

import org.junit.Test;
import org.kizombadev.eventstudio.common.EventKeys;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RealTimeValidationFilterTest {

    private Filter filter = new RealTimeValidationFilter();

    @Test
    public void testNegtiveTimeManipulation() {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put(EventKeys.TIMESTAMP, LocalDateTime.now().minusSeconds(10));
        }};
        filter.handle(json);
        assertThat(json.get(EventKeys.TIME_MANIPULATION).toString()).isEqualTo("true");
    }

    @Test
    public void testNoTimeManipulation() {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put(EventKeys.TIMESTAMP, LocalDateTime.now().minusSeconds(1));
        }};
        filter.handle(json);
        assertThat(json.get(EventKeys.TIME_MANIPULATION).toString()).isEqualTo("false");
    }

    @Test
    public void testPositiveTimeManipulation() {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put(EventKeys.TIMESTAMP, LocalDateTime.now().plusSeconds(10));
        }};
        filter.handle(json);
        assertThat(json.get(EventKeys.TIME_MANIPULATION).toString()).isEqualTo("true");
    }
}
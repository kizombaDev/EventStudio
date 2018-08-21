package org.kizombadev.eventstudio.eventpipeline.filter;

import org.junit.Before;
import org.junit.Test;
import org.kizombadev.eventstudio.common.EventKeys;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PingStatusFilterTest {

    private PingStatusFilter filter;

    @Before
    public void setup() {
        filter = new PingStatusFilter();
        Map<String, String> configuration = new HashMap<String, String>() {{
            put("key", "status");
            put("value", "ok");
            put("regex", "Antwort von ([.\\d]*): Bytes=(\\d*) Zeit[=<](\\d*)ms TTL=(\\d*)");
        }};
        filter.init(configuration);
    }

    @Test
    public void testHandle() {
        Map<EventKeys, Object> json = new HashMap<EventKeys, Object>() {{
            put(EventKeys.DATA, "Antwort von 172.217.20.227: Bytes=32 Zeit=22ms TTL=57");
        }};
        filter.handle(json);
        assertThat(json.get(EventKeys.STATUS).toString()).isEqualTo("ok");
    }

    @Test
    public void testHandleWithoutMatch() {
        Map<EventKeys, Object> json = new HashMap<EventKeys, Object>() {{
            put(EventKeys.DATA, "PING: Fehler bei der Übertragung. Allgemeiner Fehler.");
        }};
        filter.handle(json);
        assertThat(json.containsKey(EventKeys.STATUS)).isFalse();
    }

    @Test
    public void testInstanceCopy() {
        assertThat(filter.instanceCopy()).isExactlyInstanceOf(PingStatusFilter.class);
    }
}
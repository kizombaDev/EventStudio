package org.kizombadev.eventstudio.eventpipeline.filter;

import org.junit.Before;
import org.junit.Test;
import org.kizombadev.eventstudio.common.EventKeys;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RegexFilterTest {

    private RegexFilter filter;

    @Before
    public void setup() {
        filter = new RegexFilter();
        Map<String, String> configuration = new HashMap<String, String>() {{
            put("key", "status");
            put("value", "ok");
            put("regex", "Antwort von (?<ip>[.\\d]*): Bytes=(?<bytes>\\d*) Zeit[=<](?<time>\\d*)ms TTL=(?<ttl>\\d*)");
        }};
        filter.init(configuration);
    }

    @Test
    public void testHandle() {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put(EventKeys.DATA, "Antwort von 172.217.20.227: Bytes=32 Zeit=22ms TTL=57");
        }};
        filter.handle(json);
        assertThat(json.get(EventKeys.IP).toString()).isEqualTo("172.217.20.227");
        assertThat(json.get(EventKeys.BYTES).toString()).isEqualTo("32");
        assertThat(json.get(EventKeys.TIME).toString()).isEqualTo("22");
        assertThat(json.get(EventKeys.TTL).toString()).isEqualTo("57");
    }

    @Test
    public void testHandleWithoutMatch() {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put(EventKeys.DATA, "PING: Fehler bei der Übertragung. Allgemeiner Fehler.");
        }};
        filter.handle(json);
        assertThat(json.containsKey(EventKeys.IP)).isFalse();
        assertThat(json.containsKey(EventKeys.BYTES)).isFalse();
        assertThat(json.containsKey(EventKeys.TIME)).isFalse();
        assertThat(json.containsKey(EventKeys.TTL)).isFalse();
    }

    @Test
    public void testInstanceCopy() {
        assertThat(filter.instanceCopy()).isExactlyInstanceOf(RegexFilter.class);
    }

}
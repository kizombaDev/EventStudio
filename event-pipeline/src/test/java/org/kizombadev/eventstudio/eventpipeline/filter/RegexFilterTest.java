package org.kizombadev.eventstudio.eventpipeline.filter;

import org.junit.Before;
import org.junit.Test;
import org.kizombadev.eventstudio.eventpipeline.EntryKeys;

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
            put("origin", "Antwort von 172.217.20.227: Bytes=32 Zeit=22ms TTL=57");
        }};
        filter.handle(json);
        assertThat(json.get(EntryKeys.IP).toString()).isEqualTo("172.217.20.227");
        assertThat(json.get(EntryKeys.BYTES).toString()).isEqualTo("32");
        assertThat(json.get(EntryKeys.TIME).toString()).isEqualTo("22");
        assertThat(json.get(EntryKeys.TTL).toString()).isEqualTo("57");
    }

    @Test
    public void testHandleWithoutMatch() {
        Map<String, Object> json = new HashMap<String, Object>() {{
            put("origin", "PING: Fehler bei der Übertragung. Allgemeiner Fehler.");
        }};
        filter.handle(json);
        assertThat(json.containsKey(EntryKeys.IP)).isFalse();
        assertThat(json.containsKey(EntryKeys.BYTES)).isFalse();
        assertThat(json.containsKey(EntryKeys.TIME)).isFalse();
        assertThat(json.containsKey(EntryKeys.TTL)).isFalse();
    }

    @Test
    public void testInstanceCopy() {
        assertThat(filter.instanceCopy()).isExactlyInstanceOf(RegexFilter.class);
    }

}
package org.kizombadev.pipeline;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class LogEntryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private LogEntry createDefaultTestLogEntry() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.ID, "ping_google");
            put(EntryKeys.TYPE, "ping");
            put(EntryKeys.BYTES, "32");
        }};
        return new LogEntry(source);
    }

    @Test
    public void testGetType() {
        assertThat(createDefaultTestLogEntry().getType()).isEqualTo("ping");
    }

    @Test
    public void testGetId() {
        assertThat(createDefaultTestLogEntry().getId()).isEqualTo("ping_google");
    }

    @Test
    public void testGetSource() {
        assertThat(createDefaultTestLogEntry().getSource().get(EntryKeys.BYTES)).isEqualTo("32");
    }

    @Test
    public void testToString() {
        assertThat(createDefaultTestLogEntry().toString()).isEqualTo("LogEntry{id=ping_google, type=ping}");
    }

    @Test
    public void testMissingId() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.TYPE, "ping");
        }};

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The log entry contains no correct property 'id' which is mandatory.");

        new LogEntry(source);
    }

    @Test
    public void testMissingType() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.ID, "ping_google");
        }};

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The log entry contains no correct property 'type' which is mandatory.");

        new LogEntry(source);
    }
}
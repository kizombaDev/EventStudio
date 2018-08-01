package org.kizombadev.eventstudio.eventpipeline;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class EventEntryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private EventEntry createDefaultTestLogEntry() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.SOURCE_ID, "ping_google");
            put(EntryKeys.TYPE, "ping");
            put(EntryKeys.BYTES, "32");
        }};
        return new EventEntry(source);
    }

    @Test
    public void testGetType() {
        assertThat(createDefaultTestLogEntry().getType()).isEqualTo("ping");
    }

    @Test
    public void testGetId() {
        assertThat(createDefaultTestLogEntry().getSourceId()).isEqualTo("ping_google");
    }

    @Test
    public void testGetSource() {
        assertThat(createDefaultTestLogEntry().getSource().get(EntryKeys.BYTES)).isEqualTo("32");
    }

    @Test
    public void testToString() {
        assertThat(createDefaultTestLogEntry().toString()).isEqualTo("EventEntry{sourceId=ping_google, type=ping}");
    }

    @Test
    public void testMissingId() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.TYPE, "ping");
        }};

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The event entry contains no correct property 'source_id' which is mandatory.");

        new EventEntry(source);
    }

    @Test
    public void testMissingType() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.SOURCE_ID, "ping_google");
        }};

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The event entry contains no correct property 'type' which is mandatory.");

        new EventEntry(source);
    }

    @Test
    public void testWithIdAsInteger() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.TYPE, "ping");
            put(EntryKeys.SOURCE_ID, 5);
        }};

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The event entry contains no correct property 'source_id' with a String.");

        new EventEntry(source);
    }
}
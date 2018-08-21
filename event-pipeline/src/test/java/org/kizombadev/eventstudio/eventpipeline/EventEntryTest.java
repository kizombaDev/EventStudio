package org.kizombadev.eventstudio.eventpipeline;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.kizombadev.eventstudio.common.EventKeys;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


public class EventEntryTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private EventEntry createDefaultTestLogEntry() {
        Map<EventKeys, Object> source = new HashMap<EventKeys, Object>() {{
            put(EventKeys.SOURCE_ID, "ping_google");
            put(EventKeys.TYPE, "ping");
            put(EventKeys.BYTES, "32");
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
        assertThat(createDefaultTestLogEntry().getSource().get(EventKeys.BYTES)).isEqualTo("32");
    }

    @Test
    public void testToString() {
        assertThat(createDefaultTestLogEntry().toString()).isEqualTo("EventEntry{sourceId=ping_google, type=ping}");
    }

    @Test
    public void testMissingId() {
        Map<EventKeys, Object> source = new HashMap<EventKeys, Object>() {{
            put(EventKeys.TYPE, "ping");
        }};

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The event entry contains no correct property 'source_id' which is mandatory.");

        new EventEntry(source);
    }

    @Test
    public void testMissingType() {
        Map<EventKeys, Object> source = new HashMap<EventKeys, Object>() {{
            put(EventKeys.SOURCE_ID, "ping_google");
        }};

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The event entry contains no correct property 'type' which is mandatory.");

        new EventEntry(source);
    }

    @Test
    public void testWithIdAsInteger() {
        Map<EventKeys, Object> source = new HashMap<EventKeys, Object>() {{
            put(EventKeys.TYPE, "ping");
            put(EventKeys.SOURCE_ID, 5);
        }};

        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("The event entry contains no correct property 'source_id' with a String.");

        new EventEntry(source);
    }
}
package org.kizombadev.eventstudio.eventpipeline;

import com.google.common.base.MoreObjects;
import org.kizombadev.eventstudio.common.EventKeys;

import java.util.Map;

public final class EventEntry {
    private final String type;
    private final String sourceId;
    private final Map<EventKeys, Object> source;

    public EventEntry(Map<EventKeys, Object> source) {
        this.type = getStringValue(source, EventKeys.TYPE);
        this.sourceId = getStringValue(source, EventKeys.SOURCE_ID);
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public String getSourceId() {
        return sourceId;
    }

    public Map<EventKeys, Object> getSource() {
        return source;
    }

    private String getStringValue(Map<EventKeys, Object> source, EventKeys key) {
        if (!source.containsKey(key)) {
            throw new IllegalStateException(String.format("The event entry contains no correct property '%s' which is mandatory.", key));
        }

        Object typeAsString = source.get(key);
        if (typeAsString instanceof String) {
            return (String) typeAsString;
        }
        throw new IllegalStateException(String.format("The event entry contains no correct property '%s' with a String.", key));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("sourceId", sourceId)
                .add("type", type)
                .toString();
    }
}

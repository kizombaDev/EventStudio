package org.kizombadev.pipeline;

import com.google.common.base.MoreObjects;

import java.util.Map;

public final class LogEntry {
    private final String type;
    private final String id;
    private final Map<String, Object> source;

    public LogEntry(Map<String, Object> source) {
        this.type = getStringValue(source, EntryKeys.TYPE);
        this.id = getStringValue(source, EntryKeys.ID);
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getSource() {
        return source;
    }

    private String getStringValue(Map<String, Object> source, String key) {
        if (!source.containsKey(key)) {
            throw new IllegalStateException(String.format("The log entry contains no correct property '%s' which is mandatory.", key));
        }

        Object typeAsString = source.get(key);
        if (typeAsString instanceof String) {
            return (String) typeAsString;
        }
        throw new IllegalStateException(String.format("The log entry contains no correct property '%s' with a String.", key));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .toString();
    }
}

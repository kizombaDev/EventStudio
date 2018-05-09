package org.kizombadev.pipeline;

import com.google.common.base.MoreObjects;

import java.util.Map;

public class Dataset {
    private final String type;
    private final String id;
    private final Map<String, Object> source;

    public Dataset(Map<String, Object> source) {
        this.type = getStringValue(source, "type");
        this.id = getStringValue(source, "id");
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
        Object typeAsString = source.get(key);
        if (typeAsString instanceof String) {
            return (String) typeAsString;
        }
        throw new IllegalStateException(String.format("the json data contains no correct json property '%s' with a String.", key));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("type", type)
                .toString();
    }
}

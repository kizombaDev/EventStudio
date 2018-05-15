package org.kizombadev.pipeline.filter;

import java.util.Map;

public interface Filter extends Cloneable {
    void handle(Map<String, Object> json);

    void init(Map<String, String> configuration);

    Filter instanceCopy();

    default String getConfigurationOrThrow(String property, Map<String, String> configuration){
        if(!configuration.containsKey(property)) {
            throw new IllegalArgumentException(String.format("The configuration property '%s' is missing", property));
        }

        return configuration.get(property);
    }

    default Object getPropertyOrThrow(String property, Map<String, Object> entry){
        if(!entry.containsKey(property)) {
            throw new IllegalArgumentException(String.format("The property '%s' is missing in the entry", property));
        }

        return entry.get(property);
    }
}

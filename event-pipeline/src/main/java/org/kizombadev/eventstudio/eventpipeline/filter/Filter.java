package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.common.EventKeys;

import java.util.Map;

public interface Filter {
    void handle(Map<EventKeys, Object> source);

    Filter instanceCopy();

    default void init(Map<String, String> configuration) {

    }

    default String getConfigurationOrThrow(String property, Map<String, String> configuration) {
        if (!configuration.containsKey(property)) {
            throw new IllegalStateException(String.format("The configuration property '%s' is missing", property));
        }

        return configuration.get(property);
    }

    default Object getPropertyOrThrow(EventKeys property, Map<EventKeys, Object> entry) {
        if (!entry.containsKey(property)) {
            throw new IllegalStateException(String.format("The property '%s' is missing in the entry", property));
        }

        return entry.get(property);
    }
}

package org.kizombadev.eventstudio.eventpipeline.filter;

import java.util.Map;

public abstract class Filter {
    abstract void handle(Map<String, Object> source);

    abstract void init(Map<String, String> configuration);

    abstract Filter instanceCopy();

    String getConfigurationOrThrow(String property, Map<String, String> configuration) {
        if (!configuration.containsKey(property)) {
            throw new IllegalStateException(String.format("The configuration property '%s' is missing", property));
        }

        return configuration.get(property);
    }

    Object getPropertyOrThrow(String property, Map<String, Object> entry) {
        if (!entry.containsKey(property)) {
            throw new IllegalStateException(String.format("The property '%s' is missing in the entry", property));
        }

        return entry.get(property);
    }
}

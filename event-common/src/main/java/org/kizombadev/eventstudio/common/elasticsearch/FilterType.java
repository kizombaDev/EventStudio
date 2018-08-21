package org.kizombadev.eventstudio.common.elasticsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public enum FilterType {
    PRIMARY("primary"),
    SECONDARY("secondary");

    private String type;

    FilterType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static FilterType forValue(String type) {
        for (FilterType item : FilterType.values()) {
            if (Objects.equals(item.getValue(), type)) {
                return item;
            }
        }

        throw new IllegalStateException(String.format("The FilterType %s is not defined", type));
    }

    @Override
    public String toString() {
        return type;
    }

    @JsonValue
    public String getValue() {
        return this.type;
    }
}

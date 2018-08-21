package org.kizombadev.eventstudio.common.elasticsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public enum MappingType {
    KEYWORD_TYPE("keyword"),
    DATE_TYPE("date"),
    IP_TYPE("ip"),
    INTEGER_TYPE("integer"),
    TEXT_TYPE("text"),
    BOOLEAN_TYPE("boolean");

    private String type;

    MappingType(String type) {
        this.type = type;
    }

    @JsonCreator
    public static MappingType forValue(String type) {
        for (MappingType item : MappingType.values()) {
            if (Objects.equals(item.getValue(), type)) {
                return item;
            }
        }

        throw new IllegalStateException(String.format("The MappingType %s is not defined", type));
    }

    @Override
    public String toString() {
        return type;
    }

    @JsonValue
    public String getValue() {
        return  this.type;
    }
}


package org.kizombadev.eventstudio.common.elasticsearch;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

public enum FilterOperation {
    EQUALS("equals"),
    GREATER_THEN("gt"),
    GREATER_THEN_OR_EQUAL("gte"),
    LESS_THEN("lt"),
    LESS_THEN_OR_EQUAL("lte"),

    EXIST("exist"),
    NOT_EXIST("not_exist"),
    CONTAINS("contains");

    private String operation;

    FilterOperation(String operation) {
        this.operation = operation;
    }

    @JsonCreator
    public static FilterOperation forValue(String operation) {
        for (FilterOperation item : FilterOperation.values()) {
            if (Objects.equals(item.getValue(), operation)) {
                return item;
            }
        }

        throw new IllegalStateException(String.format("The FilterOperation %s is not defined", operation));
    }

    @JsonValue
    public String getValue() {
        return operation;
    }

    @Override
    public String toString() {
        return operation;
    }
}


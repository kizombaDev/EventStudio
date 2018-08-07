package org.kizombadev.eventstudio.common.elasticsearch;

public enum FilterType {
    PRIMARY("primary"), SECONDARY("secondary");

    private final String value;

    FilterType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }
}

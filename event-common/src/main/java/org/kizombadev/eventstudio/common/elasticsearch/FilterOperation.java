package org.kizombadev.eventstudio.common.elasticsearch;

public final class FilterOperation {
    public static final String EQUALS = "equals";
    public static final String GREATER_THEN = "gt";
    public static final String GREATER_THEN_OR_EQUAL = "gte";
    public static final String LESS_THEN = "lt";
    public static final String LESS_THEN_OR_EQUAL = "lte";
    public static final String EXIST = "exist";
    public static final String NOT_EXIST = "not_exist";

    private FilterOperation() {
        //nothing todo
    }
}

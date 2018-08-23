package org.kizombadev.eventstudio.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Objects;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class EventKeys {
    private static final ConcurrentMap<String, EventKeys> MAP = new ConcurrentHashMap<>();

    public static final EventKeys DATA = EventKeys.forValue("data");
    public static final EventKeys IP = EventKeys.forValue("ip");
    public static final EventKeys TTL = EventKeys.forValue("ttl");
    public static final EventKeys TIME = EventKeys.forValue("time");
    public static final EventKeys BYTES = EventKeys.forValue("bytes");
    public static final EventKeys TIMESTAMP = EventKeys.forValue("timestamp");
    public static final EventKeys TYPE = EventKeys.forValue("type");
    public static final EventKeys SOURCE_ID = EventKeys.forValue("source_id");
    public static final EventKeys SEQUENTIAL_TIME_ID = EventKeys.forValue("sequential_time_id");
    public static final EventKeys TIME_MANIPULATION = EventKeys.forValue("time_manipulation");
    public static final EventKeys STATUS = EventKeys.forValue("status");


    private final String value;

    private EventKeys(String value) {
        this.value = value;
    }

    @JsonCreator
    public static EventKeys forValue(String value) {

        EventKeys result = MAP.get(value);

        if (result == null) {
            EventKeys temp = new EventKeys(value);
            result = MAP.putIfAbsent(value, new EventKeys(value));

            if (result == null) {
                result = temp;
            }
        }

        return result;
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventKeys eventKeys = (EventKeys) o;
        return Objects.equal(value, eventKeys.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
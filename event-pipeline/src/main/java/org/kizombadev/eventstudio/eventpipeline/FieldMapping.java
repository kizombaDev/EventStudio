package org.kizombadev.eventstudio.eventpipeline;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.MappingType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class FieldMapping {

    private static final Map<EventKeys, MappingType> MAPPING = new HashMap<>();

    static {
        MAPPING.put(EventKeys.SOURCE_ID, MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.TYPE, MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.TIMESTAMP, MappingType.DATE_TYPE);
        MAPPING.put(EventKeys.DATA, MappingType.TEXT_TYPE);

        MAPPING.put(EventKeys.STATUS, MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.TIME, MappingType.INTEGER_TYPE);
        MAPPING.put(EventKeys.TTL, MappingType.INTEGER_TYPE);
        MAPPING.put(EventKeys.IP, MappingType.IP_TYPE);
        MAPPING.put(EventKeys.BYTES, MappingType.INTEGER_TYPE);
        MAPPING.put(EventKeys.TIME_MANIPULATION, MappingType.BOOLEAN_TYPE);

        MAPPING.put(EventKeys.SEQUENTIAL_TIME_ID, MappingType.INTEGER_TYPE);
        MAPPING.put(EventKeys.forValue("length"), MappingType.INTEGER_TYPE);
        MAPPING.put(EventKeys.forValue("request_method"), MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.forValue("path"), MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.forValue("request_version"), MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.forValue("response_status"), MappingType.INTEGER_TYPE);
        MAPPING.put(EventKeys.forValue("host"), MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.forValue("referrer"), MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.forValue("user_agent"), MappingType.KEYWORD_TYPE);
        MAPPING.put(EventKeys.forValue("remote_log_name"), MappingType.KEYWORD_TYPE);
    }

    private FieldMapping() {
        //nothing to do
    }

    /**
     * Returns the FieldType or null
     */
    public static MappingType getTypeOfField(EventKeys fieldName) {
        return MAPPING.get(fieldName);
    }

    public static boolean isFieldOfType(EventKeys fieldName, MappingType type) {
        MappingType actualType = MAPPING.get(fieldName);

        if (actualType == null) {
            return false;
        }

        return Objects.equals(actualType, type);
    }

    public static boolean contains(EventKeys fieldName) {
        return MAPPING.containsKey(fieldName);
    }
}
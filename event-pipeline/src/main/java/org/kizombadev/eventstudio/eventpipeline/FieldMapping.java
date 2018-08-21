package org.kizombadev.eventstudio.eventpipeline;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.MappingType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class FieldMapping {

    private static final Map<String, MappingType> MAPPING = new HashMap<>();

    static {
        MAPPING.put("source_id", MappingType.KEYWORD_TYPE);
        MAPPING.put("type", MappingType.KEYWORD_TYPE);
        MAPPING.put("timestamp", MappingType.DATE_TYPE);
        MAPPING.put("data", MappingType.TEXT_TYPE);

        MAPPING.put("status", MappingType.KEYWORD_TYPE);
        MAPPING.put("time", MappingType.INTEGER_TYPE);
        MAPPING.put("ttl", MappingType.INTEGER_TYPE);
        MAPPING.put("ip", MappingType.IP_TYPE);
        MAPPING.put("bytes", MappingType.INTEGER_TYPE);
        MAPPING.put(EventKeys.TIME_MANIPULATION, MappingType.BOOLEAN_TYPE);

        MAPPING.put("length", MappingType.INTEGER_TYPE);
        MAPPING.put("request_method", MappingType.KEYWORD_TYPE);
        MAPPING.put("path", MappingType.KEYWORD_TYPE);
        MAPPING.put("request_version", MappingType.KEYWORD_TYPE);
        MAPPING.put("response_status", MappingType.INTEGER_TYPE);
        MAPPING.put("host", MappingType.KEYWORD_TYPE);
        MAPPING.put("referrer", MappingType.KEYWORD_TYPE);
        MAPPING.put("user_agent", MappingType.KEYWORD_TYPE);
        MAPPING.put("remote_log_name", MappingType.KEYWORD_TYPE);
        MAPPING.put("sequential_time_id", MappingType.INTEGER_TYPE);
    }

    private FieldMapping() {
        //nothing to do
    }

    /**
     * Returns the FieldType or null
     */
    public static MappingType getTypeOfField(String fieldName) {
        return MAPPING.get(fieldName);
    }

    public static boolean isFieldOfType(String fieldName, MappingType type) {
        MappingType actualType = MAPPING.get(fieldName);

        if (actualType == null) {
            return false;
        }

        return Objects.equals(actualType, type);
    }

    public static boolean contains(String fieldName) {
        return MAPPING.containsKey(fieldName);
    }
}
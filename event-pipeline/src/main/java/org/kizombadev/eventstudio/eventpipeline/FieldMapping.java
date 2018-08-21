package org.kizombadev.eventstudio.eventpipeline;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.FieldTypes;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class FieldMapping {

    private static final Map<String, String> MAPPING = new HashMap<>();

    static {
        MAPPING.put("source_id", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("type", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("timestamp", FieldTypes.DATE_TYPE);
        MAPPING.put("data", FieldTypes.TEXT_TYPE);

        MAPPING.put("status", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("time", FieldTypes.INTEGER_TYPE);
        MAPPING.put("ttl", FieldTypes.INTEGER_TYPE);
        MAPPING.put("ip", FieldTypes.IP_TYPE);
        MAPPING.put("bytes", FieldTypes.INTEGER_TYPE);
        MAPPING.put(EventKeys.TIME_MANIPULATION, FieldTypes.BOOLEAN_TYPE);

        MAPPING.put("length", FieldTypes.INTEGER_TYPE);
        MAPPING.put("request_method", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("path", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("request_version", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("response_status", FieldTypes.INTEGER_TYPE);
        MAPPING.put("host", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("referrer", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("user_agent", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("remote_log_name", FieldTypes.KEYWORD_TYPE);
        MAPPING.put("sequential_time_id", FieldTypes.INTEGER_TYPE);
    }

    private FieldMapping() {
        //nothing to do
    }

    /**
     * Returns the FieldType or null
     */
    public static String getTypeOfField(String fieldName){
        return MAPPING.get(fieldName);
    }

    public static boolean isFieldOfType(String fieldName, String type) {
        String actualType = MAPPING.get(fieldName);

        if(actualType == null) {
            return false;
        }

        return Objects.equals(actualType, type);
    }

    public static boolean contains(String fieldName){
        return MAPPING.containsKey(fieldName);
    }
}
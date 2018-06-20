package org.kizombadev.eventstudio.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FilterCriteria {
    private FilterCriteria() {}

    public static JsonObject of(String field, String value, String type, String operator) {
        JsonObject filterCriterion = new JsonObject();
        filterCriterion.addProperty("field", field);
        filterCriterion.addProperty("value", value);
        filterCriterion.addProperty("type", type);
        filterCriterion.addProperty("operator", operator);
        return filterCriterion;
    }

    public static JsonArray arrayOf(JsonObject... objects) {
        JsonArray result = new JsonArray();
        for(JsonObject object : objects) {
            result.add(object);
        }
        return result;
    }
}

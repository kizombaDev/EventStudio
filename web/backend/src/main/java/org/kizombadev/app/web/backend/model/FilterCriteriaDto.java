package org.kizombadev.app.web.backend.model;

public class FilterCriteriaDto {
    private String field;
    private String value;
    private String type;
    private String operator;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

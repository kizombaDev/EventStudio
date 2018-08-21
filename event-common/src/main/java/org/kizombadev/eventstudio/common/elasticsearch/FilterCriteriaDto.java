package org.kizombadev.eventstudio.common.elasticsearch;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.kizombadev.eventstudio.common.EventKeys;

public class FilterCriteriaDto {
    private EventKeys field;
    private String value;
    private FilterType type;
    private FilterOperation operator;

    public FilterCriteriaDto() {
        //nothing to do
    }

    public FilterCriteriaDto(EventKeys field, String value, FilterType type, FilterOperation operator) {
        this.field = field;
        this.value = value;
        this.type = type;
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EventKeys getField() {
        return field;
    }

    public void setField(EventKeys field) {
        this.field = field;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    public FilterOperation getOperator() {
        return operator;
    }

    public void setOperator(FilterOperation operator) {
        this.operator = operator;
    }


    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("field", field)
                .add("value", value)
                .add("type", type)
                .add("operator", operator)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterCriteriaDto that = (FilterCriteriaDto) o;
        return Objects.equal(field, that.field) &&
                Objects.equal(value, that.value) &&
                Objects.equal(type, that.type) &&
                Objects.equal(operator, that.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(field, value, type, operator);
    }
}

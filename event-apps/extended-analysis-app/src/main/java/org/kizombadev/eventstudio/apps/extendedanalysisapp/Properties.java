package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.kizombadev.eventstudio.common.PropertyHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("extended-analysis")
public class Properties {
    private static final String PROPERTY_ROOT = "extended-analysis.";

    private String eventType;
    private String indicatorField;
    private String referenceField;

    public String getEventType() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "eventType", eventType);
        return eventType;
    }

    public void setEventType(String eventType) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "eventType", eventType);
        this.eventType = eventType;
    }

    public String getIndicatorField() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "indicatorField", indicatorField);
        return indicatorField;
    }

    public void setIndicatorField(String indicatorField) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "indicatorField", indicatorField);
        this.indicatorField = indicatorField;
    }

    public String getReferenceField() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "referenceField", referenceField);
        return referenceField;
    }

    public void setReferenceField(String referenceField) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "referenceField", referenceField);
        this.referenceField = referenceField;
    }
}

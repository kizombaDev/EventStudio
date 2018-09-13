package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.kizombadev.eventstudio.common.PropertyHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("house-keeping")
public class Properties {
    private static final String PROPERTY_ROOT = "house-keeping.";

    private long maxIndexMbSize;

    public long getMaxIndexMbSize() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "maxIndexMbSize", maxIndexMbSize);
        return maxIndexMbSize;
    }

    public void setMaxIndexMbSize(long maxIndexMbSize) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "maxIndexMbSize", maxIndexMbSize);
        this.maxIndexMbSize = maxIndexMbSize;
    }
}

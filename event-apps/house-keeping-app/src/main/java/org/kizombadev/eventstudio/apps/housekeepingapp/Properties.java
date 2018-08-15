package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.kizombadev.eventstudio.common.PropertyHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("house-keeping")
public class Properties {
    private static final String PROPERTY_ROOT = "house-keeping.";

    private int storageTime;

    public int getStorageTime() {
        PropertyHelper.validateNotEmpty(PROPERTY_ROOT + "storageTime", storageTime);
        return storageTime;
    }

    public void setStorageTime(int storageTime) {
        PropertyHelper.logPropertyValue(PROPERTY_ROOT + "storageTime", storageTime);
        this.storageTime = storageTime;
    }
}

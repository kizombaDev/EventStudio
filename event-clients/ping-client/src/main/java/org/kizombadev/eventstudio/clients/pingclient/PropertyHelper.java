package org.kizombadev.eventstudio.clients.pingclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class PropertyHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyHelper.class);

    private PropertyHelper() {
    }

    public static void validateNotEmpty(String propertyName, Object propertyValue) {
        String message = String.format("the property %s has no value", propertyName);
        if (propertyValue == null) {
            throw new IllegalStateException(message);
        }

        if (propertyValue instanceof String && StringUtils.isEmpty(propertyValue)) {
            throw new IllegalStateException(message);
        }
    }

    public static void logPropertyValue(String propertyName, Object propertyValue) {
        LOGGER.info(String.format("Property-Name: %s Property-Value: %s", propertyName, propertyValue));
    }
}

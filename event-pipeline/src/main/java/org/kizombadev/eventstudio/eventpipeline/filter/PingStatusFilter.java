package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("PingStatusFilter")
public class PingStatusFilter implements Filter {
    private static final String REGEX_CONFIGURATION = "regex";
    private static final String KEY_CONFIGURATION = "key";
    private static final String VALUE_CONFIGURATION = "value";

    private Pattern pattern = null;
    private EventKeys key = null;
    private String value = null;

    @Override
    public void handle(Map<EventKeys, Object> json) {
        String origin = String.valueOf(getPropertyOrThrow(EventKeys.DATA, json));
        Matcher matcher = pattern.matcher(origin);
        if (!matcher.find()) {
            return;
        }

        json.put(key, value);
    }

    @Override
    public void init(Map<String, String> configuration) {
        pattern = Pattern.compile(getConfigurationOrThrow(REGEX_CONFIGURATION, configuration));
        key = EventKeys.forValue(getConfigurationOrThrow(KEY_CONFIGURATION, configuration));
        value = getConfigurationOrThrow(VALUE_CONFIGURATION, configuration);
    }

    @Override
    public Filter instanceCopy() {
        return new PingStatusFilter();
    }
}

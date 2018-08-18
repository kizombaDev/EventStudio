package org.kizombadev.eventstudio.eventpipeline.filter;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component("AccessLogCleanerFilter")
public class AccessLogCleanerFilter extends Filter {
    @Override
    public void handle(Map<String, Object> json) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            String value = entry.getValue().toString();
            //TODO only remove the character on integer fields
            value = value.replace("-", "");
            value = value.replace(" ", "");

            if (value.isEmpty()) {
                json.put(entry.getKey(), "");
            }

            if ("ip".equals(entry.getKey())) {
                json.put(entry.getKey(), entry.getValue().toString().replace("x", "0"));
            }
        }
    }

    @Override
    public void init(Map<String, String> configuration) {
        //nothing to do
    }

    @Override
    public Filter instanceCopy() {
        return new AccessLogCleanerFilter();
    }
}

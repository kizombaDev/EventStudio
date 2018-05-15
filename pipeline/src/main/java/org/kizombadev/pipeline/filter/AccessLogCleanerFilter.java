package org.kizombadev.pipeline.filter;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component("AccessLogCleanerFilter")
public class AccessLogCleanerFilter implements Filter {
    @Override
    public void handle(Map<String, Object> json) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            String value = entry.getValue().toString();
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
        //nothing tod do
    }

    @Override
    public Filter instanceCopy() {
        return new AccessLogCleanerFilter();
    }
}

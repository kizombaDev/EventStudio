package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.MappingType;
import org.kizombadev.eventstudio.eventpipeline.FieldMapping;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("AccessLogCleanerFilter")
public class AccessLogCleanerFilter implements Filter {
    @Override
    public void handle(Map<String, Object> json) {
        for (Map.Entry<String, Object> entry : json.entrySet()) {

            if (FieldMapping.isFieldOfType(entry.getKey(), MappingType.INTEGER_TYPE)) {
                String value = entry.getValue().toString();
                value = value.replace("-", "");
                value = value.replace(" ", "");
                json.put(entry.getKey(), value);
            }

            if (EventKeys.IP.equals(entry.getKey())) {
                json.put(entry.getKey(), entry.getValue().toString().replace("x", "0"));
            }
        }
    }

    @Override
    public Filter instanceCopy() {
        return new AccessLogCleanerFilter();
    }
}

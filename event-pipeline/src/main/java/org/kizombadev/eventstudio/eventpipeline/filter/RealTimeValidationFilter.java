package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component("RealTimeValidationFilter")
public class RealTimeValidationFilter implements Filter {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Override
    public void handle(Map<EventKeys, Object> json) {
        String timestampAsString = json.get(EventKeys.TIMESTAMP).toString();
        LocalDateTime timestamp = LocalDateTime.parse(timestampAsString, DATE_TIME_FORMATTER);

        boolean timeManipulation = timestamp.isBefore(LocalDateTime.now().minusSeconds(5))
                || timestamp.isAfter(LocalDateTime.now().plusSeconds(5));
        json.put(EventKeys.TIME_MANIPULATION, timeManipulation);
    }

    @Override
    public Filter instanceCopy() {
        return new RealTimeValidationFilter();
    }
}

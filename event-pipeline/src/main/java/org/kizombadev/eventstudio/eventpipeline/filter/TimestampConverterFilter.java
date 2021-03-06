package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Component("TimestampConverterFilter")
public class TimestampConverterFilter implements Filter {

    private static final String PATTERN_CONFIGURATION = "pattern";
    private static final String LANGUAGE_CONFIGURATION = "lang";

    private DateTimeFormatter inputDateTimeFormatter;
    private DateTimeFormatter outputDateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME;


    @Override
    public void handle(Map<EventKeys, Object> json) {
        String timestampAsString = getPropertyOrThrow(EventKeys.TIMESTAMP, json).toString();
        LocalDateTime timestamp = LocalDateTime.parse(timestampAsString, inputDateTimeFormatter);
        json.put(EventKeys.TIMESTAMP, timestamp.format(outputDateTimeFormatter));
    }

    @Override
    public void init(Map<String, String> configuration) {
        String pattern = getConfigurationOrThrow(PATTERN_CONFIGURATION, configuration);
        String lang = getConfigurationOrThrow(LANGUAGE_CONFIGURATION, configuration);

        inputDateTimeFormatter = DateTimeFormatter.ofPattern(pattern).withLocale(new Locale(lang));
    }

    @Override
    public Filter instanceCopy() {
        return new TimestampConverterFilter();
    }
}

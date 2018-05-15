package org.kizombadev.pipeline.filter;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

@Component("TimestampConverterFilter")
public class TimestampConverterFilter implements Filter {

    private DateTimeFormatter DATE_TIME_FORMATTER;
    private DateTimeFormatter A = DateTimeFormatter.ISO_DATE_TIME;


    @Override
    public void handle(Map<String, Object> json) {
        String timestampAsString = json.get("timestamp").toString();
        LocalDateTime timestamp = LocalDateTime.parse(timestampAsString, DATE_TIME_FORMATTER);
        json.put("timestamp", timestamp.format(A));
    }

    @Override
    public void init(Map<String, String> configuration) {
        String pattern = configuration.get("pattern");
        String lang = configuration.get("lang");

        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(pattern).withLocale(new Locale(lang));
    }

    @Override
    public Filter instanceCopy() {
        return new TimestampConverterFilter();
    }
}

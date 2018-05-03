package org.kizombadev.pipeline.filter.impl;

import org.kizombadev.pipeline.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PingFailedRegexFilter implements Filter {
    private static final Pattern PING_PATTERN = Pattern.compile("PING: Fehler bei der Übertragung. Allgemeiner Fehler.");
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(Map<String, Object> json) {

        String origin = String.valueOf(json.get("origin"));
        Matcher matcher = PING_PATTERN.matcher(origin);
        if (!matcher.find()) {
            return;
        }

        json.put("status", "failed");
    }
}

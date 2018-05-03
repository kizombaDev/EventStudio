package org.kizombadev.pipeline.filter.impl;

import org.kizombadev.pipeline.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PingSuccessfulRegexFilter implements Filter {
    private static final Pattern PING_PATTERN = Pattern.compile("Antwort von ([.\\d]*): Bytes=(\\d*) Zeit=(\\d*)ms TTL=(\\d*)");
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void handle(Map<String, Object> json) {

        String origin = String.valueOf(json.get("origin"));
        Matcher matcher = PING_PATTERN.matcher(origin);
        if (!matcher.find()) {
            return;
        }

        if (matcher.groupCount() != 4) {
            log.warn("the group count is wrong");
            return;
        }

        json.put("ip", matcher.group(1));
        json.put("bytes", Integer.valueOf(matcher.group(2)));
        json.put("time", Integer.valueOf(matcher.group(3)));
        json.put("ttl", Integer.valueOf(matcher.group(4)));
        json.put("status", "ok");
    }
}

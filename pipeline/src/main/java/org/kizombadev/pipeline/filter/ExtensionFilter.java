package org.kizombadev.pipeline.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("ExtensionFilter")
public class ExtensionFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Pattern pattern = null;
    private String key = null;
    private String value = null;

    @Override
    public void handle(Map<String, Object> json) {
        String origin = String.valueOf(json.get("origin"));
        Matcher matcher = pattern.matcher(origin);
        if (!matcher.find()) {
            return;
        }

        json.put(key, value);
    }

    @Override
    public void init(Map<String, String> configuration) {
        pattern = Pattern.compile(configuration.get("regex"));
        key = configuration.get("key");
        value = configuration.get("value");
    }

    @Override
    public Filter instanceCopy() {
        return new ExtensionFilter();
    }
}

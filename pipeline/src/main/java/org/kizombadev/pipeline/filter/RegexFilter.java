package org.kizombadev.pipeline.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("RegexFilter")
public class RegexFilter implements Filter {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Pattern pattern = null;
    private Map<Integer, String> groupDefinitions = new HashMap<>();

    @Override
    public void handle(Map<String, Object> json) {
        String origin = String.valueOf(json.get("origin"));
        Matcher matcher = pattern.matcher(origin);
        if (!matcher.find()) {
            return;
        }

        //TODO assert group index

        for (Map.Entry<Integer, String> pair : groupDefinitions.entrySet()) {
            json.put(pair.getValue(), matcher.group(pair.getKey()));

        }
    }

    @Override
    public void init(Map<String, String> configuration) {
        pattern = Pattern.compile(configuration.get("regex"));
        initGroupDefinitions(configuration);

    }

    private void initGroupDefinitions(Map<String, String> configuration) {
        for (int index = 0; index < 1000; index++) {
            if (configuration.containsKey("group-" + index)) {
                groupDefinitions.put(index, configuration.get("group-" + index));
            }
        }
    }

    @Override
    public Filter instanceCopy() {
        return new RegexFilter();
    }
}

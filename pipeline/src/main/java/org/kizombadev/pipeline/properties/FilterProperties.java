package org.kizombadev.pipeline.properties;

import com.google.common.base.MoreObjects;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties("pipeline")
public class FilterProperties {
    private static final String PROPERTY_ROOT = "pipeline.";

    private final List<FilterConfig> filter = new ArrayList<>();

    public List<FilterConfig> getFilter() {
        return filter;
    }

    public static class FilterConfig {
        private static final String PROPERTY_ROOT_FILTER = PROPERTY_ROOT + "filter.";
        private final List<KeyValue> configuration = new ArrayList<>();
        private String name;
        private String type;

        public String getName() {
            PropertyHelper.validateNotEmpty(PROPERTY_ROOT_FILTER + "name", name);
            return name;
        }

        public void setName(String name) {
            PropertyHelper.logPropertyValue(PROPERTY_ROOT_FILTER + "name", name);
            this.name = name;
        }

        public String getType() {
            PropertyHelper.validateNotEmpty(PROPERTY_ROOT_FILTER + "type", type);
            return type;
        }

        public void setType(String type) {
            PropertyHelper.logPropertyValue(PROPERTY_ROOT_FILTER + "type", type);
            this.type = type;
        }

        public List<KeyValue> getConfiguration() {
            return configuration;
        }

        public Map<String, String> getConfigurationAsMap() {
            Map<String, String> result = new HashMap<>();
            configuration.forEach(x -> result.put(x.getKey(), x.getValue()));
            return result;
        }


        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("name", name)
                    .add("type", type)
                    .toString();
        }

        public static class KeyValue {
            private static final String PROPERTY_ROOT_FILTER_CONFIGURATION = PROPERTY_ROOT_FILTER + "configuration.";
            private String key;
            private String value;

            public String getValue() {
                PropertyHelper.validateNotEmpty(PROPERTY_ROOT_FILTER_CONFIGURATION + "value", value);
                return value;
            }

            public void setValue(String value) {
                PropertyHelper.logPropertyValue(PROPERTY_ROOT_FILTER_CONFIGURATION + "value", value);
                this.value = value;
            }

            public String getKey() {
                PropertyHelper.validateNotEmpty(PROPERTY_ROOT_FILTER_CONFIGURATION + "key", key);
                return key;
            }

            public void setKey(String key) {
                PropertyHelper.logPropertyValue(PROPERTY_ROOT_FILTER_CONFIGURATION + "key", key);
                this.key = key;
            }

            @Override
            public String toString() {
                return MoreObjects.toStringHelper(this)
                        .add("key", key)
                        .add("value", value)
                        .toString();
            }
        }
    }
}
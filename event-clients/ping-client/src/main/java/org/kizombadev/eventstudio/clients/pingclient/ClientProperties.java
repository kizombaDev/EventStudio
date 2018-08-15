package org.kizombadev.eventstudio.clients.pingclient;

import com.google.common.base.MoreObjects;
import org.kizombadev.eventstudio.common.PropertyHelper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties("ping-client")
public class ClientProperties {
    private static final String PROPERTY_ROOT = "ping-client.";

    private final List<ClientConfig> clients = new ArrayList<>();

    public List<ClientConfig> getClients() {
        return clients;
    }

    public static class ClientConfig {
        private static final String PROPERTY_ROOT_FILTER = PROPERTY_ROOT + "clients.";
        private final List<KeyValue> configuration = new ArrayList<>();
        private String name;
        private String id;

        public String getName() {
            PropertyHelper.validateNotEmpty(PROPERTY_ROOT_FILTER + "name", name);
            return name;
        }

        public void setName(String name) {
            PropertyHelper.logPropertyValue(PROPERTY_ROOT_FILTER + "name", name);
            this.name = name;
        }

        public String getId() {
            PropertyHelper.validateNotEmpty(PROPERTY_ROOT_FILTER + "id", id);
            return id;
        }

        public void setId(String id) {
            PropertyHelper.logPropertyValue(PROPERTY_ROOT_FILTER + "id", id);
            this.id = id;
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
                    .add("id", id)
                    .toString();
        }

        public static class KeyValue {
            private static final String PROPERTY_ROOT_FILTER_CONFIGURATION = PROPERTY_ROOT_FILTER + "configuration.";
            private String key;
            private String value;

            String getValue() {
                PropertyHelper.validateNotEmpty(PROPERTY_ROOT_FILTER_CONFIGURATION + "value", value);
                return value;
            }

            public void setValue(String value) {
                PropertyHelper.logPropertyValue(PROPERTY_ROOT_FILTER_CONFIGURATION + "value", value);
                this.value = value;
            }

            String getKey() {
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

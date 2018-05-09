package org.kizombadev.app.clients.client.output;

import java.util.Map;

public interface ClientOutput {
    void send(Map<String, Object> data);
}

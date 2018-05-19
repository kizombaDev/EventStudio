package org.kizombadev.app.clients.ping.output;

import java.util.Map;

public interface ClientOutput {
    void send(Map<String, Object> data);
}

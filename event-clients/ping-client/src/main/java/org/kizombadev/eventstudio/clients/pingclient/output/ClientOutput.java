package org.kizombadev.eventstudio.clients.pingclient.output;

import java.util.Map;

public interface ClientOutput {
    void send(Map<String, Object> data);
}

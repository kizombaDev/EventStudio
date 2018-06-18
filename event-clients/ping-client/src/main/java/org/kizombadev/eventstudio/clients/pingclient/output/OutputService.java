package org.kizombadev.eventstudio.clients.pingclient.output;

import java.util.Map;

public interface OutputService {
    void handleSend(Map<String, Object> data);
}

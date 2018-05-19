package org.kizombadev.app.clients.ping.output;

import java.util.Map;

public interface OutputService {
    void handleSend(Map<String, Object> data);
}

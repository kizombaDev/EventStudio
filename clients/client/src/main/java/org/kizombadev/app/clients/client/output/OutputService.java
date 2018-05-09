package org.kizombadev.app.clients.client.output;

import java.util.Map;

public interface OutputService {
    void handleSend(Map<String, Object> data);
}

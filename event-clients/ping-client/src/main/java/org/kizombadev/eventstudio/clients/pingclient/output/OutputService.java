package org.kizombadev.eventstudio.clients.pingclient.output;

import org.kizombadev.eventstudio.common.EventKeys;

import java.util.Map;

public interface OutputService {
    void handleSend(Map<EventKeys, Object> data);
}

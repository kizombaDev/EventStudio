package org.kizombadev.eventstudio.clients.pingclient.output;

import org.kizombadev.eventstudio.common.EventKeys;

import java.util.Map;

public interface ClientOutput {
    void send(Map<EventKeys, Object> data);
}

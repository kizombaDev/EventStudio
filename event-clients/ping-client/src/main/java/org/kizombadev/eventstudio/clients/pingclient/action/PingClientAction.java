package org.kizombadev.eventstudio.clients.pingclient.action;

import org.kizombadev.eventstudio.clients.pingclient.output.OutputService;

import java.util.Map;

public interface PingClientAction extends Runnable {
    void init(String id, OutputService outputService, Map<String, String> configurationAsMap);

    PingClientAction instanceCopy();
}

package org.kizombadev.app.clients.ping.operation;


import org.kizombadev.app.clients.ping.output.OutputService;

import java.util.Map;

public interface ClientOperation extends Runnable {
    void init(String id, OutputService outputService, Map<String, String> configurationAsMap);

    ClientOperation instanceCopy();
}

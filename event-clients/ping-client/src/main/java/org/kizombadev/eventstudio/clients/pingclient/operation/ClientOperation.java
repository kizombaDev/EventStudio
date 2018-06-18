package org.kizombadev.eventstudio.clients.pingclient.operation;



import org.kizombadev.eventstudio.clients.pingclient.output.OutputService;

import java.util.Map;

public interface ClientOperation extends Runnable {
    void init(String id, OutputService outputService, Map<String, String> configurationAsMap);

    ClientOperation instanceCopy();
}

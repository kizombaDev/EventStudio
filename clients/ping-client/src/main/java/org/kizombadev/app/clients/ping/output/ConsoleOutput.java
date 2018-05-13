package org.kizombadev.app.clients.ping.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsoleOutput implements ClientOutput {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void send(Map<String, Object> data) {
        log.info(data.toString());
    }
}

package org.kizombadev.pipeline.output.impl;

import org.kizombadev.pipeline.output.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConsoleOutput implements Output {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void write(Map<String, Object> data) {
        log.info(data.toString());
    }
}

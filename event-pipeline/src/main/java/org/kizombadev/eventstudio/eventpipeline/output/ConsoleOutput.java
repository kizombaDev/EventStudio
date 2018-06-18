package org.kizombadev.eventstudio.eventpipeline.output;

import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsoleOutput implements Output {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void write(EventEntry data) {
        log.info(data.getSource().toString());
    }

    @Override
    public void write(List<EventEntry> logEntries) {
        for (EventEntry data : logEntries) {
            write(data);
        }
    }
}

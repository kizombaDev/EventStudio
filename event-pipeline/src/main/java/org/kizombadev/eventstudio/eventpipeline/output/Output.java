package org.kizombadev.eventstudio.eventpipeline.output;

import org.kizombadev.eventstudio.eventpipeline.EventEntry;

import java.util.List;

public interface Output {
    void write(List<EventEntry> logEntries);
}

package org.kizombadev.eventstudio.eventpipeline.output;

import org.kizombadev.eventstudio.eventpipeline.LogEntry;

import java.util.List;

public interface Output {
    void write(List<LogEntry> logEntries);
}

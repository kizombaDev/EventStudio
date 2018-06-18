package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.eventpipeline.LogEntry;

public interface FilterService {
    void handle(LogEntry logEntry);
}

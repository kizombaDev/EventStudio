package org.kizombadev.pipeline.filter;

import org.kizombadev.pipeline.LogEntry;

public interface FilterService {
    void handle(LogEntry logEntry);
}

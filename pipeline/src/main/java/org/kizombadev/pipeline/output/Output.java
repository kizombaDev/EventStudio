package org.kizombadev.pipeline.output;

import org.kizombadev.pipeline.LogEntry;

import java.util.List;

public interface Output {
    void write(List<LogEntry> logEntries);
}

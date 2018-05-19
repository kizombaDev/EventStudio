package org.kizombadev.pipeline.controller;

import org.kizombadev.pipeline.LogEntry;

import java.util.List;

public interface PipelineService {
    void run(List<LogEntry> logEntries);
}

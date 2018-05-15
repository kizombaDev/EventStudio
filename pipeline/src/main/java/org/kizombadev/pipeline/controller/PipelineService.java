package org.kizombadev.pipeline.controller;

import org.kizombadev.pipeline.LogEntry;

import java.util.List;
import java.util.Map;

public interface PipelineService {
    void run(Map<String, Object> data);

    void run(List<LogEntry> logEntries);
}

package org.kizombadev.eventstudio.eventpipeline.controller;

import org.kizombadev.eventstudio.eventpipeline.LogEntry;

import java.util.List;

public interface PipelineService {
    void run(List<LogEntry> logEntries);
}

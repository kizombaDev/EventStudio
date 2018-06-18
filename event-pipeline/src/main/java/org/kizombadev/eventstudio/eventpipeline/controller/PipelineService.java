package org.kizombadev.eventstudio.eventpipeline.controller;

import org.kizombadev.eventstudio.eventpipeline.EventEntry;

import java.util.List;

public interface PipelineService {
    void run(List<EventEntry> logEntries);
}

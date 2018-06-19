package org.kizombadev.eventstudio.eventpipeline.controller;

import org.kizombadev.eventstudio.eventpipeline.output.Output;
import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.kizombadev.eventstudio.eventpipeline.filter.FilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PipelineServiceImpl implements PipelineService {

    private List<Output> outputs;
    private FilterService filterService;

    @Autowired
    public PipelineServiceImpl(List<Output> outputs, FilterService filterService) {
        this.outputs = outputs;
        this.filterService = filterService;
    }

    @Override
    public void run(List<EventEntry> logEntries) {
        for (EventEntry eventEntry : logEntries) {
            filterService.handle(eventEntry);
        }

        for (Output output : outputs) {
            output.write(logEntries);
        }
    }
}

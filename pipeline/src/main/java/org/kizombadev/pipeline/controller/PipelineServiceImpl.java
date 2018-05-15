package org.kizombadev.pipeline.controller;

import org.kizombadev.pipeline.LogEntry;
import org.kizombadev.pipeline.filter.FilterService;
import org.kizombadev.pipeline.output.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public void run(Map<String, Object> data) {
        LogEntry logEntry = new LogEntry(data);
        filterService.handle(logEntry);
        for (Output output : outputs) {
            output.write(logEntry);
        }
    }

    @Override
    public void run(List<LogEntry> logEntries) {
        for (LogEntry logEntry : logEntries) {
            filterService.handle(logEntry);
        }

        for (Output output : outputs) {
            output.write(logEntries);
        }

    }
}

package org.kizombadev.pipeline.controller;

import org.kizombadev.pipeline.Dataset;
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
        Dataset dataset = new Dataset(data);
        filterService.handle(dataset);
        for (Output output : outputs) {
            output.write(dataset);
        }
    }

    @Override
    public void run(List<Dataset> datasets) {
        for (Dataset dataset : datasets) {
            filterService.handle(dataset);
        }

        for (Output output : outputs) {
            output.write(datasets);
        }

    }
}

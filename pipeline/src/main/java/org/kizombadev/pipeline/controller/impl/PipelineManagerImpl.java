package org.kizombadev.pipeline.controller.impl;

import org.kizombadev.pipeline.controller.PipelineManager;
import org.kizombadev.pipeline.filter.Filter;
import org.kizombadev.pipeline.output.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PipelineManagerImpl implements PipelineManager {

    private List<Output> outputs;
    private List<Filter> filters;

    @Autowired
    public PipelineManagerImpl(List<Filter> filters, List<Output> outputs) {
        this.filters = filters;
        this.outputs = outputs;
    }

    @Override
    public void run(Map<String, Object> data) {
        for (Filter filter : filters) {
            filter.handle(data);
        }
        for (Output output : outputs) {
            output.write(data);
        }
    }
}

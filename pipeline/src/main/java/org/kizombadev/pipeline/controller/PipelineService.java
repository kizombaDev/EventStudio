package org.kizombadev.pipeline.controller;

import org.kizombadev.pipeline.Dataset;

import java.util.List;
import java.util.Map;

public interface PipelineService {
    void run(Map<String, Object> data);

    void run(List<Dataset> datasets);
}

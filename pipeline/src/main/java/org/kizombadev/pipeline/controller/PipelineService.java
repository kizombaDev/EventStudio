package org.kizombadev.pipeline.controller;

import java.util.Map;

public interface PipelineService {
    void run(Map<String, Object> data);
}

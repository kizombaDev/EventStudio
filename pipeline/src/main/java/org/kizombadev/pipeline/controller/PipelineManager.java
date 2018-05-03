package org.kizombadev.pipeline.controller;

import java.util.Map;

public interface PipelineManager {
    void run(Map<String, Object> data);
}

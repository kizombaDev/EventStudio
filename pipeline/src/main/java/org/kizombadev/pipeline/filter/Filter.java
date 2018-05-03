package org.kizombadev.pipeline.filter;

import java.util.Map;

public interface Filter {
    void handle(Map<String, Object> json);
}

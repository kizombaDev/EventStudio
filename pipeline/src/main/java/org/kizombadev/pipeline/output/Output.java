package org.kizombadev.pipeline.output;

import java.util.Map;

public interface Output {
    void write(Map<String, Object> json);
}

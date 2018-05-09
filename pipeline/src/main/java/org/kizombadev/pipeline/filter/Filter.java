package org.kizombadev.pipeline.filter;

import java.util.Map;

public interface Filter extends Cloneable {
    void handle(Map<String, Object> json);

    void init(Map<String, String> configuration);

    Filter instanceCopy();
}

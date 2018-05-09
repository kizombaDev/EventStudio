package org.kizombadev.pipeline.filter;

import org.kizombadev.pipeline.Dataset;

public interface FilterService {
    void handle(Dataset data);
}

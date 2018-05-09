package org.kizombadev.pipeline.output;

import org.kizombadev.pipeline.Dataset;

public interface Output {
    void write(Dataset data);
}

package org.kizombadev.pipeline.output;

import org.kizombadev.pipeline.Dataset;

import java.util.List;

public interface Output {
    void write(Dataset data);

    void write(List<Dataset> datasets);
}

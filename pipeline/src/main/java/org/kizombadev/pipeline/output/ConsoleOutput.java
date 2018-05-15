package org.kizombadev.pipeline.output;

import org.kizombadev.pipeline.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsoleOutput implements Output {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void write(Dataset data) {
        //log.info(data.getSource().toString());
    }

    @Override
    public void write(List<Dataset> datasets) {
        for (Dataset data : datasets) {
            write(data);
        }
    }
}

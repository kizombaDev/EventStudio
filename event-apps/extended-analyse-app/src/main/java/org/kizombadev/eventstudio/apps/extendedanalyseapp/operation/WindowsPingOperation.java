package org.kizombadev.eventstudio.apps.extendedanalyseapp.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WindowsPingOperation implements ExtendedAnalyseOperation {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public void run() {
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Extended analysis executed!!!!!");
    }

}

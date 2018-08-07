package org.kizombadev.eventstudio.apps.extendedanalyseapp.scheduling;

import org.kizombadev.eventstudio.apps.extendedanalyseapp.operation.ExtendedAnalyseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class ClientSchedulerService {
    private ThreadPoolTaskScheduler taskScheduler;
    private ExtendedAnalyseOperation operation;

    @Autowired
    public ClientSchedulerService(ThreadPoolTaskScheduler taskScheduler, ExtendedAnalyseOperation operation) {
        this.taskScheduler = taskScheduler;
        this.operation = operation;
    }

    @PostConstruct
    public void enableScheduler() {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(10, TimeUnit.SECONDS);
        taskScheduler.schedule(operation, periodicTrigger);
    }
}

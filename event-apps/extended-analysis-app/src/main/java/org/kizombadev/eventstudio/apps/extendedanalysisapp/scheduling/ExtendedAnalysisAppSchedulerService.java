package org.kizombadev.eventstudio.apps.extendedanalysisapp.scheduling;

import org.kizombadev.eventstudio.apps.extendedanalysisapp.operation.ReferenceAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class ExtendedAnalysisAppSchedulerService {
    private ThreadPoolTaskScheduler taskScheduler;
    private ReferenceAnalysis operation;

    @Autowired
    public ExtendedAnalysisAppSchedulerService(ThreadPoolTaskScheduler taskScheduler, ReferenceAnalysis operation) {
        this.taskScheduler = taskScheduler;
        this.operation = operation;
    }

    @PostConstruct
    public void enableScheduler() {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(10, TimeUnit.SECONDS);
        taskScheduler.schedule(operation, periodicTrigger);
    }
}

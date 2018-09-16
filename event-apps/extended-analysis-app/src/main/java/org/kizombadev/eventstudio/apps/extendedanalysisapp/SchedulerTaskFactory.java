package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class SchedulerTaskFactory {
    private ThreadPoolTaskScheduler taskScheduler;
    private PingTimeOutlierAnalysis operation;

    @Autowired
    public SchedulerTaskFactory(ThreadPoolTaskScheduler taskScheduler, PingTimeOutlierAnalysis operation) {
        this.taskScheduler = taskScheduler;
        this.operation = operation;
    }

    @PostConstruct
    public void enableScheduler() {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(5, TimeUnit.SECONDS);
        taskScheduler.schedule(operation, periodicTrigger);
    }
}

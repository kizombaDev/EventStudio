package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class SchedulerTaskFactory {
    private ThreadPoolTaskScheduler taskScheduler;
    private DeleteEvents operation;

    @Autowired
    public SchedulerTaskFactory(ThreadPoolTaskScheduler taskScheduler, DeleteEvents operation) {
        this.taskScheduler = taskScheduler;
        this.operation = operation;
    }

    @PostConstruct
    public void enableScheduler() {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(1, TimeUnit.MINUTES);
        taskScheduler.schedule(operation, periodicTrigger);
    }
}

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
    private Properties properties;

    @Autowired
    public SchedulerTaskFactory(ThreadPoolTaskScheduler taskScheduler, DeleteEvents operation, Properties properties) {
        this.taskScheduler = taskScheduler;
        this.operation = operation;
        this.properties = properties;
    }

    @PostConstruct
    public void enableScheduler() {
        PeriodicTrigger periodicTrigger = new PeriodicTrigger(properties.getRepetitionIntervalInMinutes(), TimeUnit.MINUTES);
        taskScheduler.schedule(operation, periodicTrigger);
    }
}

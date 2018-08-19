package org.kizombadev.eventstudio.clients.pingclient.scheduling;

import org.kizombadev.eventstudio.clients.pingclient.PingClientProperties;
import org.kizombadev.eventstudio.clients.pingclient.action.PingClientAction;
import org.kizombadev.eventstudio.clients.pingclient.output.OutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class PingClientSchedulerService {
    private ApplicationContext applicationContext;
    private ThreadPoolTaskScheduler taskScheduler;
    private PingClientProperties pingClientProperties;
    private OutputService outputService;

    @Autowired
    public PingClientSchedulerService(ApplicationContext applicationContext, ThreadPoolTaskScheduler taskScheduler, PingClientProperties pingClientProperties, OutputService outputService) {
        this.applicationContext = applicationContext;
        this.taskScheduler = taskScheduler;
        this.pingClientProperties = pingClientProperties;
        this.outputService = outputService;
    }

    @PostConstruct
    public void enableScheduler() {

        for (PingClientProperties.ClientConfig config : pingClientProperties.getClients()) {
            PingClientAction operation = (PingClientAction) applicationContext.getBean(config.getName());
            operation = operation.instanceCopy();
            operation.init(config.getId(), outputService, config.getConfigurationAsMap());

            PeriodicTrigger periodicTrigger = new PeriodicTrigger(5, TimeUnit.SECONDS);
            taskScheduler.schedule(operation, periodicTrigger);
        }
    }
}

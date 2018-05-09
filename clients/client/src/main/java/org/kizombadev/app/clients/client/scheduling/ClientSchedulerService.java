package org.kizombadev.app.clients.client.scheduling;

import org.kizombadev.app.clients.client.ClientProperties;
import org.kizombadev.app.clients.client.operation.ClientOperation;
import org.kizombadev.app.clients.client.output.OutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Component
public class ClientSchedulerService {
    private ApplicationContext applicationContext;
    private ThreadPoolTaskScheduler taskScheduler;
    private ClientProperties clientProperties;
    private OutputService outputService;

    @Autowired
    public ClientSchedulerService(ApplicationContext applicationContext, ThreadPoolTaskScheduler taskScheduler, ClientProperties clientProperties, OutputService outputService) {
        this.applicationContext = applicationContext;
        this.taskScheduler = taskScheduler;
        this.clientProperties = clientProperties;
        this.outputService = outputService;
    }

    @PostConstruct
    public void enableScheduler() {

        for (ClientProperties.ClientConfig config : clientProperties.getClients()) {
            ClientOperation operation = (ClientOperation) applicationContext.getBean(config.getName());
            operation = operation.instanceCopy();
            operation.init(config.getId(), outputService, config.getConfigurationAsMap());

            PeriodicTrigger periodicTrigger = new PeriodicTrigger(5, TimeUnit.SECONDS);
            taskScheduler.schedule(operation, periodicTrigger);
        }
    }
}

package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ThreadPoolFactory {

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(20);
        threadPoolTaskScheduler.setThreadNamePrefix("HouseKeepingPool");
        return threadPoolTaskScheduler;
    }
}
package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ThreadPoolTaskSchedulerConfigTest {

    @Test
    public void threadPoolTaskScheduler() {
        ThreadPoolTaskSchedulerConfig threadPool = new ThreadPoolTaskSchedulerConfig();
        Assert.assertEquals(20, threadPool.threadPoolTaskScheduler().getPoolSize());
        Assert.assertEquals("HouseKeepingPool", threadPool.threadPoolTaskScheduler().getThreadNamePrefix());
    }
}
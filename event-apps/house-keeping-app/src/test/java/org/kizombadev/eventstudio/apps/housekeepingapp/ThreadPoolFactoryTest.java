package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.junit.Assert;
import org.junit.Test;

public class ThreadPoolFactoryTest {

    @Test
    public void threadPoolTaskScheduler() {
        ThreadPoolFactory threadPool = new ThreadPoolFactory();
        Assert.assertEquals(20, threadPool.threadPoolTaskScheduler().getPoolSize());
        Assert.assertEquals("HouseKeepingPool", threadPool.threadPoolTaskScheduler().getThreadNamePrefix());
    }
}
package org.kizombadev.eventstudio.apps.extendedanalysisapp;

import org.junit.Assert;
import org.junit.Test;

public class ThreadPoolFactoryTest {

    @Test
    public void threadPoolTaskScheduler() {
        ThreadPoolFactory threadPoolFactory = new ThreadPoolFactory();
        Assert.assertEquals(20, threadPoolFactory.threadPoolTaskScheduler().getPoolSize());
        Assert.assertEquals("AnalysisPool", threadPoolFactory.threadPoolTaskScheduler().getThreadNamePrefix());
    }
}
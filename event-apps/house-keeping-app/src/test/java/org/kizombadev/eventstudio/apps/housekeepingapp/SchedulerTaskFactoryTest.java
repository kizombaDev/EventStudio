package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class SchedulerTaskFactoryTest {

    @Mock
    private ThreadPoolTaskScheduler taskScheduler;

    @Mock
    private DeleteEvents operation;

    private SchedulerTaskFactory underTest;

    @Before
    public void init() {
        underTest = new SchedulerTaskFactory(taskScheduler, operation);
    }

    @Test
    public void enableScheduler() {
        underTest.enableScheduler();

        ArgumentCaptor<PeriodicTrigger> periodicTriggerCaptor = ArgumentCaptor.forClass(PeriodicTrigger.class);
        Mockito.verify(taskScheduler, Mockito.times(1)).schedule(Mockito.eq(operation), periodicTriggerCaptor.capture());

        Assert.assertEquals(60000, periodicTriggerCaptor.getValue().getPeriod());
        Assert.assertEquals(TimeUnit.MINUTES, periodicTriggerCaptor.getValue().getTimeUnit());
    }
}
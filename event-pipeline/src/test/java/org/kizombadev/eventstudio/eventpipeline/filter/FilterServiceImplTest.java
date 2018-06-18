package org.kizombadev.eventstudio.eventpipeline.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.eventpipeline.properties.FilterProperties;
import org.kizombadev.eventstudio.eventpipeline.EntryKeys;
import org.kizombadev.eventstudio.eventpipeline.LogEntry;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class FilterServiceImplTest {

    @Mock
    private Filter defaultFilter;

    @Mock
    private Filter filter;

    @Mock
    private ApplicationContext applicationContext;

    private FilterProperties filterProperties = new FilterProperties();

    @Before
    public void setup() {
        FilterProperties.FilterConfig filterConfig = new FilterProperties.FilterConfig();
        filterConfig.setName("TestFilter");
        filterConfig.setType("TestType");
        FilterProperties.FilterConfig.KeyValue keyValue = new FilterProperties.FilterConfig.KeyValue();
        keyValue.setKey("Foo");
        keyValue.setValue("Bar");
        filterConfig.getConfiguration().add(keyValue);
        filterProperties.getFilter().add(filterConfig);

        Mockito.when(applicationContext.getBean("TestFilter")).thenReturn(defaultFilter);
        Mockito.when(defaultFilter.instanceCopy()).thenReturn(filter);
    }

    @Test
    public void test() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.ID, "ping_google");
            put(EntryKeys.TYPE, "TestType");
        }};

        FilterService filterService = new FilterServiceImpl(applicationContext, filterProperties);
        filterService.handle(new LogEntry(source));

        Mockito.verify(filter, Mockito.times(1)).init(Mockito.any());
        Mockito.verify(filter, Mockito.times(1)).handle(source);
    }

    @Test
    public void testWithOtherFilterType() {
        Map<String, Object> source = new HashMap<String, Object>() {{
            put(EntryKeys.ID, "ping_google");
            put(EntryKeys.TYPE, "XXX");
        }};

        FilterService filterService = new FilterServiceImpl(applicationContext, filterProperties);
        filterService.handle(new LogEntry(source));

        Mockito.verify(filter, Mockito.times(1)).init(Mockito.any());
        Mockito.verify(filter, Mockito.never()).handle(Mockito.any());
    }
}
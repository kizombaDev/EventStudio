package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.kizombadev.eventstudio.eventpipeline.properties.FilterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterServiceImpl implements FilterService {

    private final List<FilterInstance> filters = new ArrayList<>();

    @Autowired
    public FilterServiceImpl(ApplicationContext applicationContext, FilterProperties filterProperties) {
        for (FilterProperties.FilterConfig filterConfigDefinition : filterProperties.getFilter()) {
            Filter filter = (Filter) applicationContext.getBean(filterConfigDefinition.getName());
            filter = filter.instanceCopy();
            filter.init(filterConfigDefinition.getConfigurationAsMap());

            FilterInstance filterInstance = new FilterInstance(filterConfigDefinition.getType(), filter);
            filters.add(filterInstance);
        }
    }

    @Override
    public void handle(EventEntry eventEntry) {
        for (FilterInstance filter : filters) {
            if (filter.getType().equals(eventEntry.getType())) {
                filter.getFilter().handle(eventEntry.getSource());
            }
        }
    }

    private static class FilterInstance {
        private final String type;
        private final Filter filter;

        FilterInstance(String type, Filter filter) {
            this.type = type;
            this.filter = filter;
        }

        String getType() {
            return type;
        }

        Filter getFilter() {
            return filter;
        }
    }
}

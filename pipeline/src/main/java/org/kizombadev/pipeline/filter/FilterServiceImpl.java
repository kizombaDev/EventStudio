package org.kizombadev.pipeline.filter;

import org.kizombadev.pipeline.Dataset;
import org.kizombadev.pipeline.properties.FilterProperties;
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
        for (FilterProperties.Filter filterDefinition : filterProperties.getFilter()) {
            Filter filter = (Filter) applicationContext.getBean(filterDefinition.getName());
            filter = filter.instanceCopy();
            filter.init(filterDefinition.getConfigrationAsMap());

            FilterInstance filterInstance = new FilterInstance(filterDefinition.getType(), filter);
            filters.add(filterInstance);
        }
    }

    @Override
    public void handle(Dataset data) {

        for (FilterInstance filter : filters) {
            if (filter.getType().equals(data.getType())) {
                filter.getFilter().handle(data.getSource());
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

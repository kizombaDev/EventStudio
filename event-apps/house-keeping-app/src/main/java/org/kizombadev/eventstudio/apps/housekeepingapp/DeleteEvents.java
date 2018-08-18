package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.kizombadev.eventstudio.common.elasticsearch.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DeleteEvents implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ElasticSearchService elasticSearchService;
    private Properties properties;

    @Autowired
    public DeleteEvents(ElasticSearchService elasticSearchService, Properties properties) {
        this.elasticSearchService = elasticSearchService;
        this.properties = properties;
    }

    @Override
    public void run() {
        long deletedEvents = elasticSearchService.deleteEvents(properties.getStorageTime());
        log.info("deletion of events successfully executed. Number of deleted events: " + deletedEvents);
    }
}

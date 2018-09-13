package org.kizombadev.eventstudio.apps.housekeepingapp;

import org.kizombadev.eventstudio.common.elasticsearch.ElasticsearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class DeleteEvents implements Runnable {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ElasticsearchService elasticSearchService;
    private Properties properties;

    @Autowired
    public DeleteEvents(ElasticsearchService elasticSearchService, Properties properties) {
        this.elasticSearchService = elasticSearchService;
        this.properties = properties;
    }

    @Override
    public void run() {
        long indexSizeInMb = elasticSearchService.getIndexSizeInMb();

        if (indexSizeInMb < properties.getMaxIndexMbSize()) {
            log.info("the current index size of {} MB is smaller then the configured max index size of {} MB -> nothing is deleted", indexSizeInMb, properties.getMaxIndexMbSize());
            return;
        }

        log.info("the current index size of {} MB is greater then the configured max index size of {} MB -> deletion of events required", indexSizeInMb, properties.getMaxIndexMbSize());

        List<Map<String, Object>> dateHistogram = elasticSearchService.getDateHistogram(new ArrayList<>());

        if(dateHistogram.isEmpty()) {
            log.info("the index contains no events -> nothing can deleted");
            return;
        }

        String date = dateHistogram.get(0).get("key").toString();
        long deletedEvents = elasticSearchService.deleteEventsOfDate(LocalDate.parse(date));
        log.info("deletion of events at the {} successfully executed. Number of deleted events: {}", date, deletedEvents);
    }
}

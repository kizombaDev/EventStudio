package org.kizombadev.eventstudio.eventpipeline.filter;

import org.kizombadev.eventstudio.eventpipeline.EventEntry;

public interface FilterService {
    void handle(EventEntry eventEntry);
}

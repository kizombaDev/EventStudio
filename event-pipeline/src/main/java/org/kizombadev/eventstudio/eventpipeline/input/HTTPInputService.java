package org.kizombadev.eventstudio.eventpipeline.input;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.eventpipeline.EventEntry;
import org.kizombadev.eventstudio.eventpipeline.controller.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
public class HTTPInputService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private PipelineService pipelineService;

    @Autowired
    public HTTPInputService(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @PostMapping(path = "/single", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void insertSingleLog(@RequestBody String json) throws IOException {
        Map<EventKeys, Object> source = OBJECT_MAPPER.readValue(json, new TypeReference<HashMap<String, Object>>() {
        });

        pipelineService.run(Collections.singletonList(new EventEntry(source)));
    }

    @PostMapping(path = "/array", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void insertMultipleLogs(@RequestBody String json) throws IOException {
        List<Map<EventKeys, Object>> source = OBJECT_MAPPER.readValue(json, new TypeReference<List<HashMap<String, Object>>>() {
        });

        pipelineService.run(source.stream().map(EventEntry::new).collect(Collectors.toList()));
    }
}
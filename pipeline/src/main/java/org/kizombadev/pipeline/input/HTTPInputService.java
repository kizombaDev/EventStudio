package org.kizombadev.pipeline.input;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kizombadev.pipeline.Dataset;
import org.kizombadev.pipeline.controller.PipelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/log")
public class HTTPInputService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private PipelineService pipelineService;

    @Autowired
    public HTTPInputService(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @RequestMapping(path = "/single", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void insertSingleLog(@RequestBody String json) throws IOException {
        Map<String, Object> map = OBJECT_MAPPER.readValue(json, new TypeReference<HashMap<String, Object>>() {
        });
        pipelineService.run(map);
    }

    @RequestMapping(path = "/multiple", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void insertMultipleLogs(@RequestBody String json) throws IOException {
        List<Map<String, Object>> logCollection = OBJECT_MAPPER.readValue(json, new TypeReference<List<HashMap<String, Object>>>() {
        });

        pipelineService.run(logCollection.stream().map(Dataset::new).collect(Collectors.toList()));
    }
}
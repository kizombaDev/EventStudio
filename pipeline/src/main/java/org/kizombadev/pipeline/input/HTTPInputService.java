package org.kizombadev.pipeline.input;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kizombadev.pipeline.controller.PipelineManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping("/rest/v1/log")
public class HTTPInputService {

    private static final ObjectMapper mapper = new ObjectMapper();
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private PipelineManager pipelineManager;

    @Autowired
    public HTTPInputService(PipelineManager pipelineManager) {
        this.pipelineManager = pipelineManager;
    }

    @RequestMapping(path = "/single", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void insertSingleLog(@RequestBody String json) {

        try {
            Map<String, Object> map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
            pipelineManager.run(map);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(path = "/multiple", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void insertMultipleLogs(@RequestBody String json) {

        try {
            List<Map<String, Object>> logCollection = mapper.readValue(json, new TypeReference<List<HashMap<String, Object>>>() {
            });

            for (Map<String, Object> data : logCollection) {
                pipelineManager.run(data);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
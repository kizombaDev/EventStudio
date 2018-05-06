package org.kizombadev.app.clients.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class PipelineClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void send(Map<String, Object> data) {
        try {
            ObjectMapper mapperObj = new ObjectMapper();

            String jsonResp = mapperObj.writeValueAsString(data);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(jsonResp, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Object> exchange = restTemplate.exchange("http://localhost:8081/api/v1/log/single", HttpMethod.POST, request, Object.class);
            log.info(jsonResp);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

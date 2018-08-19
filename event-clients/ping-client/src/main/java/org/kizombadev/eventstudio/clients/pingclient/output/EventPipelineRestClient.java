package org.kizombadev.eventstudio.clients.pingclient.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EventPipelineRestClient implements ClientOutput {
    private final RestTemplate restTemplate;

    public EventPipelineRestClient(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public void send(Map<String, Object> data) {
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            String jsonResp = mapperObj.writeValueAsString(data);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> request = new HttpEntity<>(jsonResp, headers);

            //todo use host and port of the configuration file
            restTemplate.exchange("http://localhost:8081/api/v1/events/single", HttpMethod.POST, request, Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

package org.kizombadev.eventstudio.clients.pingclient.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kizombadev.eventstudio.clients.pingclient.PingClientProperties;
import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final PingClientProperties pingClientProperties;

    @Autowired
    public EventPipelineRestClient(RestTemplateBuilder restTemplateBuilder, PingClientProperties pingClientProperties) {
        restTemplate = restTemplateBuilder.build();
        this.pingClientProperties = pingClientProperties;
    }

    public void send(Map<EventKeys, Object> data) {
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            String jsonResp = mapperObj.writeValueAsString(data);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> request = new HttpEntity<>(jsonResp, headers);

            restTemplate.exchange(pingClientProperties.getPipelineUrl(), HttpMethod.POST, request, Object.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}

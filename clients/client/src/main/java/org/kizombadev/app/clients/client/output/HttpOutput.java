package org.kizombadev.app.clients.client.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class HttpOutput implements ClientOutput {

    public void send(Map<String, Object> data) {
        try {
            ObjectMapper mapperObj = new ObjectMapper();
            String jsonResp = mapperObj.writeValueAsString(data);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
            HttpEntity<String> request = new HttpEntity<>(jsonResp, headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.exchange("http://localhost:8081/api/v1/log/single", HttpMethod.POST, request, Object.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

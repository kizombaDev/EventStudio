package org.kizombadev.eventstudio.eventpipeline.input;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.eventpipeline.controller.PipelineService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HTTPInputServiceITCase {

    private static final String BASE_URL = "/api/v1/events";

    @MockBean
    private PipelineService pipelineService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void insertSingleLog() {
        //Arrange

        //Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(BASE_URL + "/single", getPingEvent(), String.class);

        //Assert
        Mockito.verify(pipelineService, Mockito.times(1)).run(Mockito.any());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void insertMultipleLogs() {
        //Arrange

        //Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(BASE_URL + "/array", Collections.singletonList(getPingEvent()), String.class);

        //Assert
        Mockito.verify(pipelineService, Mockito.times(1)).run(Mockito.any());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private Map<String, String> getPingEvent() {
        return new HashMap<String, String>() {{
            put(EventKeys.SOURCE_ID.getValue(), "ping_fau");
            put(EventKeys.TYPE.getValue(), "ping");
        }};
    }
}
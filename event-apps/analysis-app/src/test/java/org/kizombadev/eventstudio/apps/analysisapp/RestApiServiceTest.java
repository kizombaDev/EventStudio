package org.kizombadev.eventstudio.apps.analysisapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.EventKeys;
import org.kizombadev.eventstudio.common.elasticsearch.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiServiceTest {

    private static final String BASE_URL = "/api/v1/events";

    @MockBean
    private ElasticsearchService elasticSearchService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Captor
    private ArgumentCaptor<List<FilterCriteriaDto>> filterArgumentCaptor;

    private static List<Map<String, Object>> getSampleJsonResponse() {
        List<Map<String, Object>> json = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("Foo", 5);
        json.add(map);
        return json;
    }

    private static void assertSampleJsonResponse(ResponseEntity<String> response) {
        assertThat(response.getBody()).isEqualTo("[{\"Foo\":5}]");
    }

    private static List<FilterCriteriaDto> getSampleFilterDto() {
        FilterCriteriaDto filterCriteriaDto = new FilterCriteriaDto();
        filterCriteriaDto.setField(EventKeys.SOURCE_ID);
        filterCriteriaDto.setValue("ping_google");
        filterCriteriaDto.setOperator(FilterOperation.EQUALS);
        filterCriteriaDto.setType(FilterType.PRIMARY);
        return Collections.singletonList(filterCriteriaDto);
    }

    @Test
    public void testGetElements() {
        //Arrange
        Mockito.when(elasticSearchService.getElementsByFilter(Mockito.any(), Mockito.eq(0), Mockito.eq(1))).thenReturn(getSampleJsonResponse());

        //Act
        ResponseEntity<String> response = testRestTemplate.getForEntity(BASE_URL + "/ping_google?size=1&from=0", String.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
        assertSampleJsonResponse(response);
    }

    @Test
    public void testGetFields() {
        //Arrange
        List<Map<String, String>> json = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("field", EventKeys.BYTES.getValue());
        map.put("type", MappingType.INTEGER_TYPE.getValue());
        json.add(map);
        Mockito.when(elasticSearchService.getFieldStructure()).thenReturn(json);

        //Act
        ResponseEntity<String> response = testRestTemplate.getForEntity(BASE_URL + "/structure/fields", String.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
        assertThat(response.getBody()).isEqualTo("[{\"field\":\"bytes\",\"type\":\"integer\"}]");
    }

    @Test
    public void testGetElementByFilter() {
        //Arrange
        Mockito.when(elasticSearchService.getElementsByFilter(filterArgumentCaptor.capture(), Mockito.eq(0), Mockito.eq(1))).thenReturn(getSampleJsonResponse());

        //Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(BASE_URL + "?size=1&from=0", getSampleFilterDto(), String.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
        assertSampleJsonResponse(response);
        assertFilterDto();
    }

    @Test
    public void testGetFieldValuesByType() {
        //Arrange
        Mockito.when(elasticSearchService.getTermDiagram(filterArgumentCaptor.capture(), Mockito.eq(EventKeys.SOURCE_ID), Mockito.eq(99999))).thenReturn(getSampleJsonResponse());

        //Act
        ResponseEntity<String> response = testRestTemplate.getForEntity(BASE_URL + "?type=ping&group-by=" + EventKeys.SOURCE_ID, String.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
        assertSampleJsonResponse(response);
        assertThat(filterArgumentCaptor.getValue().get(0).getField()).isEqualTo(EventKeys.TYPE);
        assertThat(filterArgumentCaptor.getValue().get(0).getValue()).isEqualTo("ping");
        assertThat(filterArgumentCaptor.getValue().get(0).getOperator()).isEqualTo(FilterOperation.EQUALS);
        assertThat(filterArgumentCaptor.getValue().get(0).getType()).isEqualTo(FilterType.PRIMARY);
    }

    @Test
    public void testGetTypeIdStructure() {
        //Arrange
        Mockito.when(elasticSearchService.getTermDiagram(filterArgumentCaptor.capture(), Mockito.eq(EventKeys.TYPE), Mockito.eq(99999))).thenReturn(getSampleJsonResponse());

        //Act
        ResponseEntity<String> response = testRestTemplate.getForEntity(BASE_URL + "/structure", String.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
        assertSampleJsonResponse(response);
        assertThat(filterArgumentCaptor.getValue()).isEmpty();
    }

    @Test
    public void testGetDateDiagram() {
        //Arrange
        Mockito.when(elasticSearchService.getDateDiagram(filterArgumentCaptor.capture())).thenReturn(getSampleJsonResponse());

        //Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(BASE_URL + "/date-diagram", getSampleFilterDto(), String.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
        assertSampleJsonResponse(response);
        assertFilterDto();
    }

    @Test
    public void testGetTermDiagram() {
        //Arrange
        Mockito.when(elasticSearchService.getTermDiagram(filterArgumentCaptor.capture(), Mockito.eq(EventKeys.forValue("path")), Mockito.eq(15))).thenReturn(getSampleJsonResponse());

        //Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(BASE_URL + "/term-diagram?term-name=path&count=15", getSampleFilterDto(), String.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON_UTF8);
        assertSampleJsonResponse(response);
        assertFilterDto();
    }

    private void assertFilterDto() {
        assertThat(filterArgumentCaptor.getValue().get(0).getField()).isEqualTo(EventKeys.SOURCE_ID);
        assertThat(filterArgumentCaptor.getValue().get(0).getValue()).isEqualTo("ping_google");
        assertThat(filterArgumentCaptor.getValue().get(0).getOperator()).isEqualTo(FilterOperation.EQUALS);
        assertThat(filterArgumentCaptor.getValue().get(0).getType()).isEqualTo(FilterType.PRIMARY);
    }
}

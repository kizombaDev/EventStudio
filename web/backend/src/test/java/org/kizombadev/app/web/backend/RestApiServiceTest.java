package org.kizombadev.app.web.backend;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiServiceTest {

    @MockBean
    private ElasticSearchService elasticSearchService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testGetElements() {
        //Arrange
        List<Map<String, Object>> json = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("Foo", 5);
        json.add(map);
        Mockito.when(elasticSearchService.getElementsByFilter(Mockito.any(), Mockito.eq(0), Mockito.eq(1))).thenReturn(json);

        //Act
        ResponseEntity<String> response = testRestTemplate.getForEntity("/api/v1/logs/ping_google?size=1&from=0", String.class);

        //Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("[{\"Foo\":5}]");
    }
}

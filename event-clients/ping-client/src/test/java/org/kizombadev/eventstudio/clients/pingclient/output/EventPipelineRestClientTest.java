package org.kizombadev.eventstudio.clients.pingclient.output;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kizombadev.eventstudio.common.EventKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.Map;

import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(value = EventPipelineRestClient.class)
@ComponentScan(
        basePackages = "org.kizombadev.eventstudio.clients",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "org.kizombadev.eventstudio.clients.pingclient.scheduling.*"))
public class EventPipelineRestClientTest {

    @Autowired
    private EventPipelineRestClient eventPipelineRestClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    public void test() {
        //Arrange
        server.expect(once(), requestTo("http://localhost:8081/api/v1/events/single")).andRespond(withSuccess("", MediaType.APPLICATION_JSON));
        Map<String, Object> map = ImmutableMap.of(EventKeys.SOURCE_ID, "google", EventKeys.TYPE, "ping");

        //Act
        eventPipelineRestClient.send(map);

        //Assert
        server.verify();
    }
}

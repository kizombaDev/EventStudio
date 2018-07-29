package org.kizombadev.eventstudio.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.apache.http.entity.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anyOf;

public class EventPipelineITCase {

    private ApplicationRunner applicationRunner;

    @Before
    public void init() {
        given().port(9200).delete("/eventstudio").then().statusCode(anyOf(is(HttpStatus.OK.value()), is(HttpStatus.NOT_FOUND.value())));

        applicationRunner = new ApplicationRunner();
        applicationRunner.initEventPipelineApplication();
        applicationRunner.initRestApiApplication();
    }

    @After
    public void cleanup() {
        if (applicationRunner != null) {
            applicationRunner.close();
        }
    }

    @Test
    public void testSingleInsert() {

        //Arrange
        JsonObject ping = new JsonObject();
        ping.addProperty("id", "ping_fau");
        ping.addProperty("type", "ping");
        ping.addProperty("timestamp", "2013-03-06T01:55:42+00:00");
        ping.addProperty("origin", "Antwort von 172.217.18.3: Bytes=32 Zeit=26ms TTL=57");

        //Act
        given().port(8081).contentType(ContentType.APPLICATION_JSON.toString()).body(ping.toString())
                .when().post("/api/v1/event/single")
                .then().assertThat().statusCode(HttpStatus.OK.value());

        //Assert
        applicationRunner.waitForResult();


        Response response = given().port(8082)
                .when().get("/api/v1/events/ping_fau?size=1&from=0");
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("id", hasItems("ping_fau"))
                .body("type", hasItems("ping"))
                .body("time", hasItems("26"))
                .body("ttl", hasItems("57"))
                .body("bytes", hasItems("32"))
                .body("status", hasItems("ok"));
    }

    @Test
    public void testMultipleInsert() {

        //Arrange
        JsonObject ping = new JsonObject();
        ping.addProperty("id", "ping_fau");
        ping.addProperty("type", "ping");
        ping.addProperty("timestamp", "2013-03-06T01:00:00+00:00");
        ping.addProperty("origin", "Antwort von 172.217.18.3: Bytes=32 Zeit=26ms TTL=57");

        JsonObject ping2 = new JsonObject();
        ping2.addProperty("id", "ping_fau");
        ping2.addProperty("type", "ping");
        ping2.addProperty("timestamp", "2013-03-06T02:00:00+00:00");
        ping2.addProperty("origin", "Antwort von 172.217.18.3: Bytes=33 Zeit=27ms TTL=58");

        JsonArray pings = new JsonArray();
        pings.add(ping);
        pings.add(ping2);

        //Act
        given().port(8081).contentType(ContentType.APPLICATION_JSON.toString()).body(pings.toString())
                .when().post("/api/v1/event/multiple")
                .then().assertThat().statusCode(HttpStatus.OK.value());

        //Assert
        applicationRunner.waitForResult();

        Response response = given().port(8082)
                .when().get("/api/v1/events/ping_fau?size=2&from=0");
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("id", hasItems("ping_fau", "ping_fau"))
                .body("type", hasItems("ping", "ping"))
                .body("time", hasItems("26", "27"))
                .body("ttl", hasItems("57", "58"))
                .body("bytes", hasItems("32", "33"))
                .body("status", hasItems("ok", "ok"));
    }
}

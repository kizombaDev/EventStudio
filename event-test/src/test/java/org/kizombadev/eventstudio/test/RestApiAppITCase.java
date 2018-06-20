package org.kizombadev.eventstudio.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.response.Response;
import org.apache.http.entity.ContentType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.anyOf;

public class RestApiAppITCase {

    private static ApplicationRunner applicationRunner;

    @BeforeClass
    public static void init() {
        given().port(9200).delete("/ping").then().statusCode(anyOf(is(HttpStatus.OK.value()), is(HttpStatus.NOT_FOUND.value())));

        applicationRunner = new ApplicationRunner();
        applicationRunner.initEventPipelineApplication();
        applicationRunner.initRestApiApplication();

        insertEvents();
    }

    private static void insertEvents() {
        JsonObject ping = new JsonObject();
        ping.addProperty("id", "ping_google");
        ping.addProperty("type", "ping");
        ping.addProperty("timestamp", "2013-01-01T01:00:00+00:00");
        ping.addProperty("origin", "Antwort von 172.217.21.3: Bytes=32 Zeit=26ms TTL=57");

        JsonObject ping2 = new JsonObject();
        ping2.addProperty("id", "ping_fau");
        ping2.addProperty("type", "ping");
        ping2.addProperty("timestamp", "2013-01-02T01:00:00+00:00");
        ping2.addProperty("origin", "Antwort von 172.217.18.3: Bytes=33 Zeit=27ms TTL=58");

        JsonObject ping3 = new JsonObject();
        ping3.addProperty("id", "ping_fau");
        ping3.addProperty("type", "ping");
        ping3.addProperty("timestamp", "2013-01-02T02:00:00+00:00");
        ping3.addProperty("origin", "Antwort von 172.217.18.3: Bytes=33 Zeit=28ms TTL=58");

        JsonObject ping4 = new JsonObject();
        ping4.addProperty("id", "ping_fau");
        ping4.addProperty("type", "ping");
        ping4.addProperty("timestamp", "2013-01-03T02:00:00+00:00");
        ping4.addProperty("origin", "Antwort von 172.217.18.3: Bytes=32 Zeit=29ms TTL=58");

        JsonArray pings = new JsonArray();
        pings.add(ping);
        pings.add(ping2);
        pings.add(ping3);
        pings.add(ping4);

        given().port(8081).contentType(ContentType.APPLICATION_JSON.toString()).body(pings.toString())
                .when().post("/api/v1/event/multiple")
                .then().assertThat().statusCode(HttpStatus.OK.value());

        applicationRunner.waitForResult();
    }

    @AfterClass
    public static void cleanup() {
        if (applicationRunner != null) {
            applicationRunner.close();
        }
    }

    @Test
    public void testGetElementsWithSize() {
        //Act
        Response response = given().port(8082)
                .when()
                .queryParam("size", 2)
                .queryParam("from", 0)
                .pathParam("id", "ping_fau")
                .get("/api/v1/events/{id}");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("id", hasItems("ping_fau"))
                .body("time", hasItems("28", "29"));
    }

    @Test
    public void testGetElementsWithFrom() {
        //Act
        Response response = given().port(8082)
                .when()
                .queryParam("size", 2)
                .queryParam("from", 1)
                .pathParam("id", "ping_fau")
                .get("/api/v1/events/{id}");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("id", hasItems("ping_fau"))
                .body("time", hasItems("27", "28"));
    }

    @Test
    public void testGetElementsWithId() {
        //Act
        Response response = given().port(8082)
                .when()
                .queryParam("size", 1)
                .queryParam("from", 0)
                .pathParam("id", "ping_google")
                .get("/api/v1/events/{id}");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("id", hasItems("ping_google"))
                .body("time", hasItems("26"));
    }

    @Test
    public void testGetElementsByFilterTimeEquals27() {
        //Arrange
        JsonArray filterCriteria = FilterCriteria.arrayOf(FilterCriteria.of("time", "27", "primary", "equals"));

        //Act
        Response response = given().port(8082).contentType(ContentType.APPLICATION_JSON.toString()).body(filterCriteria.toString())
                .when()
                .queryParam("size", 100)
                .queryParam("from", 0)
                .post("/api/v1/events");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("id", hasItems("ping_fau"))
                .body("time", hasItems("27"));
    }

    @Test
    public void testGetElementsByFilterTimeGreaterThen27() {
        //Arrange
        JsonArray filterCriteria = FilterCriteria.arrayOf(FilterCriteria.of("time", "27", "primary", "gt"));

        //Act
        Response response = given().port(8082).contentType(ContentType.APPLICATION_JSON.toString()).body(filterCriteria.toString())
                .when()
                .queryParam("size", 100)
                .queryParam("from", 0)
                .post("/api/v1/events");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("id", hasItems("ping_fau"))
                .body("time", hasItems("28", "29"));
    }

    @Test
    public void testGetElementsByFilterTimeLessThenEquals27AndIdEqualsPingGoogle() {
        //Arrange
        JsonArray filterCriteria = FilterCriteria.arrayOf(
                FilterCriteria.of("time", "27", "primary", "lte"),
                FilterCriteria.of("id", "ping_google", "primary", "equals"));

        //Act
        Response response = given().port(8082).contentType(ContentType.APPLICATION_JSON.toString()).body(filterCriteria.toString())
                .when()
                .queryParam("size", 100)
                .queryParam("from", 0)
                .post("/api/v1/events");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("id", hasItems("ping_google"))
                .body("time", hasItems("26"));
    }

    @Test
    public void testGetDateHistogram() {
        //Arrange
        JsonArray filterCriteria = FilterCriteria.arrayOf(
                FilterCriteria.of("time", "29", "primary", "lt"),
                FilterCriteria.of("ttl", "58", "secondary", "equals"));

        //Act
        Response response = given().port(8082).contentType(ContentType.APPLICATION_JSON.toString()).body(filterCriteria.toString())
                .when()
                .post("/api/v1/events/date-histogram");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("key[0]", is("01-01-2013"))
                .body("primary_count[0]", is(1))
                .body("secondary_count[0]", is(0))
                .body("key[1]", is( "02-01-2013"))
                .body("primary_count[1]", is(2))
                .body("secondary_count[1]", is( 2));
    }

    @Test
    public void testGetFieldValuesByType() {
        //Act
        Response response = given().port(8082)
                .when()
                .queryParam("type", "ping")
                .queryParam("group-by", "id")
                .get("/api/v1/events");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("key", hasItems("ping_fau", "ping_google"))
                .body("count", hasItems(3, 1));
    }

    @Test
    public void testGetFields() {
        //Act
        Response response = given().port(8082)
                .when()
                .get("/api/v1/events/structure/fields");

        //Assert
        response.then().log().ifValidationFails().assertThat().statusCode(HttpStatus.OK.value());
        response.then()
                .body("field[0]", is("bytes"))
                .body("type[0]", is("integer"))
                .body("field[1]", is("id"))
                .body("type[1]", is("keyword"))
                .body("field[2]", is("ip"))
                .body("type[2]", is("ip"))
                .body("field[3]", is("origin"))
                .body("type[3]", is("text"))
                .body("field[4]", is("status"))
                .body("type[4]", is("keyword"))
                .body("field[5]", is("time"))
                .body("type[5]", is("integer"))
                .body("field[6]", is("timestamp"))
                .body("type[6]", is("date"))
                .body("field[7]", is("ttl"))
                .body("type[7]", is("integer"))
                .body("field[8]", is("type"))
                .body("type[8]", is("keyword"));
    }
}

package org.example;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class HealthCheckTests extends BaseTest {
    @Test
    public void healthCheckTest() {
        given()
                .when().
                spec(spec)
                .get("/ping")
                .then()
                .assertThat()
                .statusCode(201);
    }
}

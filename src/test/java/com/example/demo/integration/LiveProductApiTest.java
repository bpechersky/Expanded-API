
package com.example.demo.integration;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LiveProductApiTest {

    @Test
    public void createProductAgainstRunningApp() {
        RestAssured.baseURI = "http://localhost:8080";

        given()
                .contentType("application/json")
                .body("{\"name\": \"LiveTestProduct\", \"price\": 42.42}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .body("name", equalTo("LiveTestProduct"))
                .body("price", equalTo(42.42f));
    }
}

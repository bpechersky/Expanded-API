
package com.example.demo.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LiveProductApiTest {

    @Test
    public void createAndGetProductById() {
        RestAssured.baseURI = "http://localhost:8080";

        // Create a new product
        Response postResponse = given()
                .contentType("application/json")
                .body("{\"name\": \"TestProductWithId\", \"price\": 55.55}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .body("name", equalTo("TestProductWithId"))
                .body("price", equalTo(55.55f))
                .extract().response();

        int productId = postResponse.jsonPath().getInt("id");

        // Get the product by ID
        given()
                .contentType("application/json")
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("TestProductWithId"))
                .body("price", equalTo(55.55f));
    }
}

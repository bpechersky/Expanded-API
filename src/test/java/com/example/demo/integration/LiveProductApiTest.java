
package com.example.demo.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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
    @Test
    public void getAllProducts() {
        RestAssured.baseURI = "http://localhost:8080";

        given()
                .contentType("application/json")
                .when()
                .get("/api/products")
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("size()", greaterThan(0));
    }
    @Test
    public void updateProductById() {
        RestAssured.baseURI = "http://localhost:8080";

        // Create a product first
        Response postResponse = given()
                .contentType("application/json")
                .body("{\"name\": \"OriginalProduct\", \"price\": 10.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = postResponse.jsonPath().getInt("id");

        // Update the product
        given()
                .contentType("application/json")
                .body("{\"name\": \"UpdatedProduct\", \"price\": 99.99}")
                .when()
                .put("/api/products/" + productId)
                .then()
                .statusCode(200)
                .body("id", equalTo(productId))
                .body("name", equalTo("UpdatedProduct"))
                .body("price", equalTo(99.99f));
    }

}

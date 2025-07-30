package com.example.demo.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LiveProductApiTest {

    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.ROhlL_pf4HiRBoz4bP95Lz4UnGMVPOlpsNKl7DiHeLQ";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    public void createAndGetProductById() {
        // Create product
        Response postResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"TestProductWithId\", \"price\": 55.55}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .body("name", equalTo("TestProductWithId"))
                .body("price", equalTo(55.55f))
                .extract().response();

        int productId = postResponse.jsonPath().getInt("id");

        // Get product by ID
        given()
                .header("Authorization", TOKEN)
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
        given()
                .header("Authorization", TOKEN)
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
        // Create product
        Response postResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"OriginalProduct\", \"price\": 10.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract().response();

        int productId = postResponse.jsonPath().getInt("id");

        // Update product
        given()
                .header("Authorization", TOKEN)
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

    @Test
    public void deleteProductById() {
        // Create product
        Response postResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"ProductToDelete\", \"price\": 25.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract().response();

        int productId = postResponse.jsonPath().getInt("id");

        // Delete product
        given()
                .header("Authorization", TOKEN)
                .when()
                .delete("/api/products/" + productId)
                .then()
                .statusCode(204);

        // Try to fetch deleted product
        given()
                .header("Authorization", TOKEN)
                .when()
                .get("/api/products/" + productId)
                .then()
                .statusCode(anyOf(is(404), is(204)));
    }
}

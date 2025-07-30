package com.example.demo.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductReviewsTest {
    @Test
    public void postReview() {
        RestAssured.baseURI = "http://localhost:8080";

        // First create a product to review
        Response productResponse = given()
                .contentType("application/json")
                .body("{\"name\": \"Reviewable Product\", \"price\": 45.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        // Now post a review for the created product
        given()
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 5, \"comment\": \"Excellent product!\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200)
                .body("productId", equalTo(productId))
                .body("rating", equalTo(5))
                .body("comment", equalTo("Excellent product!"));
    }
    @Test
    public void getAllReviews() {
        RestAssured.baseURI = "http://localhost:8080";

        // Create a product to associate with the review
        Response productResponse = given()
                .contentType("application/json")
                .body("{\"name\": \"Reviewed Product\", \"price\": 65.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        // Create a review for that product
        given()
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 4, \"comment\": \"Good value.\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200);

        // GET all reviews and check that response is not empty
        given()
                .contentType("application/json")
                .when()
                .get("/api/reviews")
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("size()", greaterThan(0));
    }

    @Test
    public void getReviewById() {
        RestAssured.baseURI = "http://localhost:8080";

        // Create a product to associate with the review
        Response productResponse = given()
                .contentType("application/json")
                .body("{\"name\": \"Target Product\", \"price\": 88.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        // Create a review
        Response reviewResponse = given()
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 3, \"comment\": \"Average quality\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200)
                .extract().response();

        int reviewId = reviewResponse.jsonPath().getInt("id");

        // GET the review by ID
        given()
                .contentType("application/json")
                .when()
                .get("/api/reviews/" + reviewId)
                .then()
                .statusCode(200)
                .body("id", equalTo(reviewId))
                .body("productId", equalTo(productId))
                .body("rating", equalTo(3))
                .body("comment", equalTo("Average quality"));
    }
    @Test
    public void updateReviewById() {
        RestAssured.baseURI = "http://localhost:8080";

        // Create a product to associate with the review
        Response productResponse = given()
                .contentType("application/json")
                .body("{\"name\": \"Updatable Product\", \"price\": 77.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        // Create a review
        Response reviewResponse = given()
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 2, \"comment\": \"Needs improvement\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200)
                .extract().response();

        int reviewId = reviewResponse.jsonPath().getInt("id");

        // Update the review
        given()
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 5, \"comment\": \"Updated: Excellent!\"}")
                .when()
                .put("/api/reviews/" + reviewId)
                .then()
                .statusCode(200)
                .body("id", equalTo(reviewId))
                .body("productId", equalTo(productId))
                .body("rating", equalTo(5))
                .body("comment", equalTo("Updated: Excellent!"));
    }
    @Test
    public void deleteReviewById() {
        RestAssured.baseURI = "http://localhost:8080";

        // Create a product to associate with the review
        Response productResponse = given()
                .contentType("application/json")
                .body("{\"name\": \"Product For Deletion\", \"price\": 59.99}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        // Create a review
        Response reviewResponse = given()
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 1, \"comment\": \"Delete me\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200)
                .extract().response();

        int reviewId = reviewResponse.jsonPath().getInt("id");

        // Delete the review
        given()
                .when()
                .delete("/api/reviews/" + reviewId)
                .then()
                .statusCode(200);

        // Confirm the review is gone (expect 404 or 204 depending on your API)
        given()
                .when()
                .get("/api/reviews/" + reviewId)
                .then()
                .statusCode(404);
    }

}

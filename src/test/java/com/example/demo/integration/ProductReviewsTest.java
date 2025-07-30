package com.example.demo.integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductReviewsTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.ROhlL_pf4HiRBoz4bP95Lz4UnGMVPOlpsNKl7DiHeLQ"; // 🔐 Replace with real token

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void postReview() {
        String uniqueName = "Reviewable Product " + UUID.randomUUID();

        Response productResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"" + uniqueName + "\", \"price\": 45.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 5, \"comment\": \"Excellent product!\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(201)
                .body("productId", equalTo(productId))
                .body("rating", equalTo(5))
                .body("comment", equalTo("Excellent product!"));
    }

    @Test
    public void getAllReviews() {
        Response productResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"Reviewed Product\", \"price\": 65.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 4, \"comment\": \"Good value.\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200);

        given()
                .header("Authorization", TOKEN)
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
        Response productResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"Target Product\", \"price\": 88.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        Response reviewResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 3, \"comment\": \"Average quality\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200)
                .extract().response();

        int reviewId = reviewResponse.jsonPath().getInt("id");

        given()
                .header("Authorization", TOKEN)
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
        Response productResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"Updatable Product\", \"price\": 77.00}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        Response reviewResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 2, \"comment\": \"Needs improvement\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200)
                .extract().response();

        int reviewId = reviewResponse.jsonPath().getInt("id");

        given()
                .header("Authorization", TOKEN)
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
        Response productResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"Product For Deletion\", \"price\": 59.99}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().response();

        int productId = productResponse.jsonPath().getInt("id");

        Response reviewResponse = given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 1, \"comment\": \"Delete me\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(200)
                .extract().response();

        int reviewId = reviewResponse.jsonPath().getInt("id");

        given()
                .header("Authorization", TOKEN)
                .when()
                .delete("/api/reviews/" + reviewId)
                .then()
                .statusCode(204);

        given()
                .header("Authorization", TOKEN)
                .when()
                .get("/api/reviews/" + reviewId)
                .then()
                .statusCode(404);
    }
}

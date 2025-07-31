package com.example.demo.integration;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductReviewsTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.ROhlL_pf4HiRBoz4bP95Lz4UnGMVPOlpsNKl7DiHeLQ";

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void postReview() {
        String uniqueName = "Reviewable Product " + UUID.randomUUID();

        int productId = createProduct(uniqueName, 45.00);

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
        int productId = createProduct("Reviewed Product", 65.00);

        given()
                .header("Authorization", TOKEN)
                .contentType("application/json")
                .body("{\"productId\": " + productId + ", \"rating\": 4, \"comment\": \"Good value.\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(201);

        given()
                .header("Authorization", TOKEN)
                .when()
                .get("/api/reviews")
                .then()
                .statusCode(200)
                .body("$", not(empty()))
                .body("size()", greaterThan(0));
    }

    @Test
    public void getReviewById() {
        int productId = createProduct("Target Product", 88.00);

        int reviewId = createReview(productId, 3, "Average quality");

        given()
                .header("Authorization", TOKEN)
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
        int productId = createProduct("Updatable Product", 77.00);

        int reviewId = createReview(productId, 2, "Needs improvement");

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
        int productId = createProduct("Product For Deletion", 59.99);
        int reviewId = createReview(productId, 1, "Delete me");

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

    @Test
    public void testReviewForNonExistentProduct() {
        given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body("{\"productId\": 99999, \"rating\": 5, \"comment\": \"invalid product id\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(400)
                .body(containsString("does not exist"));
    }

    private int createProduct(String name, double price) {
        Response productResponse = given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body("{\"name\": \"" + name + "\", \"price\": " + price + "}")
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract().response();
        return productResponse.jsonPath().getInt("id");
    }

    private int createReview(int productId, int rating, String comment) {
        Response reviewResponse = given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body("{\"productId\": " + productId + ", \"rating\": " + rating + ", \"comment\": \"" + comment + "\"}")
                .when()
                .post("/api/reviews")
                .then()
                .statusCode(201)
                .extract().response();
        return reviewResponse.jsonPath().getInt("id");
    }
}

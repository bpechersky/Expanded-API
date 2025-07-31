package com.example.demo.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FavoriteApiTest {

    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.ROhlL_pf4HiRBoz4bP95Lz4UnGMVPOlpsNKl7DiHeLQ"; // Replace with real token
    private static Long createdFavoriteId;

    @Test(priority = 1)
    public void testCreateFavorite() {
        String requestBody = "{ \"userId\": 1, \"productId\": 1 }";

        Response response = given()
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/favorites")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1))
                .body("productId", equalTo(1))
                .extract().response();

        createdFavoriteId = ((Number) response.path("id")).longValue();
        System.out.println("Created Favorite ID: " + createdFavoriteId);
    }

    @Test(priority = 2, dependsOnMethods = "testCreateFavorite")
    public void testGetFavoriteById() {
        given()
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .when()
                .get("/api/favorites/" + createdFavoriteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdFavoriteId.intValue()));
    }

    @Test(priority = 3)
    public void testGetAllFavorites() {
        given()
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .when()
                .get("/api/favorites")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test(priority = 4, dependsOnMethods = "testCreateFavorite")
    public void testUpdateFavorite() {
        String updatedBody = "{ \"userId\": 1, \"productId\": 2 }";

        given()
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .contentType(ContentType.JSON)
                .body(updatedBody)
                .when()
                .put("/api/favorites/" + createdFavoriteId)
                .then()
                .statusCode(200)
                .body("productId", equalTo(2));
    }

    @Test(priority = 5)
    public void testGetNonExistentFavorite() {
        given()
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .when()
                .get("/api/favorites/999999")
                .then()
                .statusCode(404);
    }

    @Test(priority = 6, dependsOnMethods = "testCreateFavorite")
    public void testDeleteFavorite() {
        given()
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .when()
                .delete("/api/favorites/" + createdFavoriteId)
                .then()
                .statusCode(204);
    }

    @Test(priority = 7)
    public void testDeleteNonExistentFavorite() {
        given()
                .header("Authorization", "Bearer " + BEARER_TOKEN)
                .when()
                .delete("/api/favorites/999999")
                .then()
                .statusCode(404);
    }
}

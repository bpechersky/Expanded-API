package com.example.demo.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FavoriteApiTest {

    private static Long createdFavoriteId;

    @Test(priority = 1)
    public void testCreateFavorite() {
        String requestBody = "{ \"userId\": 1, \"productId\": 1 }";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/favorites")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1))
                .body("productId", equalTo(1))
                .extract().response();

        createdFavoriteId = ((Number) response.path("userId")).longValue();
        System.out.println("Created Favorite ID: " + createdFavoriteId);


    }

    @Test(priority = 2, dependsOnMethods = "testCreateFavorite")
    public void testGetFavoriteById() {
        given()
                .when()
                .get("/api/favorites/" + createdFavoriteId)
                .then()
                .statusCode(200)
                .body("id", equalTo(createdFavoriteId.intValue()));
    }



    @Test(priority = 3)
    public void testGetAllFavorites() {
        given()
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
                .when()
                .get("/api/favorites/999999")
                .then()
                .statusCode(404);
    }

    @Test(priority = 6, dependsOnMethods = "testCreateFavorite")
    public void testDeleteFavorite() {
        given()
                .when()
                .delete("/api/favorites/" + createdFavoriteId)
                .then()
                .statusCode(204);
    }

    @Test(priority = 7)
    public void testDeleteNonExistentFavorite() {
        given()
                .when()
                .delete("/api/favorites/999999")
                .then()
                .statusCode(404);
    }
}

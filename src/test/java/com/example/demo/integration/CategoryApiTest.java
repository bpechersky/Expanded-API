package com.example.demo.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CategoryApiTest {

    private static Long categoryId;

    @Test
    public void testCreateCategory() {
        String payload = "{ \"name\": \"Electronics\" }";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/api/categories")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Electronics"))
                .extract().response();

        categoryId = response.jsonPath().getLong("id");
    }

    @Test(dependsOnMethods = "testCreateCategory")
    public void testGetCategoryById() {
        given()
                .when()
                .get("/api/categories/" + categoryId)
                .then()
                .statusCode(200)
                .body("id", equalTo(categoryId.intValue()))
                .body("name", equalTo("Electronics"));
    }

    @Test(dependsOnMethods = "testGetCategoryById")
    public void testGetAllCategories() {
        given()
                .when()
                .get("/api/categories")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(dependsOnMethods = "testGetAllCategories")
    public void testUpdateCategory() {
        String updatedPayload = "{ \"name\": \"Updated Electronics\" }";

        given()
                .contentType(ContentType.JSON)
                .body(updatedPayload)
                .when()
                .put("/api/categories/" + categoryId)
                .then()
                .statusCode(200)
                .body("id", equalTo(categoryId.intValue()))
                .body("name", equalTo("Updated Electronics"));
    }

    @Test(dependsOnMethods = "testUpdateCategory")
    public void testDeleteCategory() {
        given()
                .when()
                .delete("/api/categories/" + categoryId)
                .then()
                .statusCode(204);
    }

    @Test(dependsOnMethods = "testDeleteCategory")
    public void testGetNonExistentCategory() {
        given()
                .when()
                .get("/api/categories/" + categoryId)
                .then()
                .statusCode(404);
    }
}

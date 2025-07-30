package com.example.demo.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class InvoiceApiTest {

    private static final String BASE_URL = "http://localhost:8080/api/invoices";
    private static Long createdInvoiceId;

    @Test(priority = 1)
    public void testCreateInvoice() {
        String requestBody = """
            {
              "customerName": "Alice Smith",
              "amount": 250.0
            }
            """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue())
                .body("customerName", equalTo("Alice Smith"))
                .body("amount", equalTo(250.0f))
                .extract().response();

        createdInvoiceId = response.jsonPath().getLong("id");
    }

    @Test(priority = 2, dependsOnMethods = "testCreateInvoice")
    public void testGetAllInvoices() {
        given()
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test(priority = 3, dependsOnMethods = "testCreateInvoice")
    public void testGetInvoiceById() {
        given()
                .pathParam("id", createdInvoiceId)
                .when()
                .get(BASE_URL + "/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(createdInvoiceId.intValue()))
                .body("customerName", equalTo("Alice Smith"));
    }

    @Test(priority = 4, dependsOnMethods = "testCreateInvoice")
    public void testUpdateInvoice() {
        String updatedBody = """
            {
              "customerName": "Alice Johnson",
              "amount": 300.5
            }
            """;

        given()
                .pathParam("id", createdInvoiceId)
                .contentType(ContentType.JSON)
                .body(updatedBody)
                .when()
                .put(BASE_URL + "/{id}")
                .then()
                .statusCode(200)
                .body("customerName", equalTo("Alice Johnson"))
                .body("amount", equalTo(300.5f));
    }

    @Test(priority = 5, dependsOnMethods = "testCreateInvoice")
    public void testDeleteInvoice() {
        given()
                .pathParam("id", createdInvoiceId)
                .when()
                .delete(BASE_URL + "/{id}")
                .then()
                .statusCode(anyOf(is(200), is(204)));

        // Optional: verify it's deleted
        given()
                .pathParam("id", createdInvoiceId)
                .when()
                .get(BASE_URL + "/{id}")
                .then()
                .statusCode(404);
    }
}

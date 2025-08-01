package com.example.demo.integration;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class InvoiceApiTest {

    private static final String BASE_URL = "http://localhost:8080/api/invoices";
    private static final String TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.ROhlL_pf4HiRBoz4bP95Lz4UnGMVPOlpsNKl7DiHeLQ";

    private static Long createdInvoiceId;

    @BeforeClass
    public void setup() {
        baseURI = "http://localhost:8080";
    }

    @Test(priority = 1)
    public void testCreateInvoice() {
        String requestBody = """
        {
          "customerName": "Alice Smith",
          "items": [
            {
                "productId": 1,
                "quantity": 2,
                "unitPrice": 10
            }
          ]
        }
        """;

        Response response = given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL)
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("customerName", equalTo("Alice Smith"))
                .body("amount", equalTo(20.0f)) // 2 * $10.0 (as per controller logic)
                .extract().response();

        createdInvoiceId = response.jsonPath().getLong("id");
    }


    @Test(priority = 2, dependsOnMethods = "testCreateInvoice")
    public void testGetAllInvoices() {
        given()
                .header("Authorization", TOKEN)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test(priority = 3, dependsOnMethods = "testCreateInvoice")
    public void testGetInvoiceById() {
        given()
                .header("Authorization", TOKEN)
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
          "items": [
            {
              "productId": 1,
              "quantity": 2
            }
          ]
        }
        """;

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", TOKEN)
                .body(updatedBody)
                .when()
                .put("/api/invoices/" + createdInvoiceId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("customerName", equalTo("Alice Johnson"))
                .body("items.size()", greaterThan(0));
    }


    @Test(priority = 5, dependsOnMethods = "testCreateInvoice")
    public void testDeleteInvoice() {
        given()
                .header("Authorization", TOKEN)
                .pathParam("id", createdInvoiceId)
                .when()
                .delete(BASE_URL + "/{id}")
                .then()
                .statusCode((204));

        // Optional: verify deletion
        given()
                .header("Authorization", TOKEN)
                .pathParam("id", createdInvoiceId)
                .when()
                .get(BASE_URL + "/{id}")
                .then()
                .statusCode(404);
    }
}

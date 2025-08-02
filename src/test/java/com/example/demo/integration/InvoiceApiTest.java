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
    private static Long createdProductId;

    @BeforeClass
    public void setup() {
        baseURI = "http://localhost:8080";
    }

    @Test
    public void sanityCheck() {
        System.out.println("TestNG is running!");
    }

    @Test(priority = 1)
    public void createProductForInvoice() {
        String productPayload = """
        {
            "name": "Test Product",
            "price": 10.0
        }
        """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(productPayload)
                .header("Authorization", TOKEN)
                .when()
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract()
                .response();

        createdProductId = response.jsonPath().getLong("id");
    }

    @Test(priority = 2, dependsOnMethods = "createProductForInvoice")
    public void createInvoiceWithProduct() {
        String invoicePayload = String.format("""
        {
            "customerName": "Alice Smith",
            "items": [
                {
                    "productId": %d,
                    "quantity": 2,
                    "unitPrice": 10.0
                }
            ]
        }
        """, createdProductId);

        Response response = given()
                .contentType(ContentType.JSON)
                .body(invoicePayload)
                .header("Authorization", TOKEN)
                .when()
                .post("/api/invoices")
                .then()
                .statusCode(201)
                .body("customerName", equalTo("Alice Smith"))
                .body("amount", equalTo(20.0F)) // float comparison
                .extract().response();

        createdInvoiceId = response.jsonPath().getLong("id");
    }

    @Test(priority = 3, dependsOnMethods = "createInvoiceWithProduct")
    public void testGetAllInvoices() {
        given()
                .header("Authorization", TOKEN)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test(priority = 4, dependsOnMethods = "createInvoiceWithProduct")
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

    @Test(priority = 5, dependsOnMethods = "createInvoiceWithProduct")
    public void testUpdateInvoice() {
        String updatedBody = String.format("""
        {
          "customerName": "Alice Johnson",
          "items": [
            {
              "productId": %d,
              "quantity": 3,
              "unitPrice": 10.0
            }
          ]
        }
        """, createdProductId);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", TOKEN)
                .body(updatedBody)
                .when()
                .put("/api/invoices/" + createdInvoiceId)
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .body("$", hasKey("customerName"))
                .body("$", hasKey("amount"))
                .body("customerName", equalTo("Alice Johnson"))
                .body("items", notNullValue())
                .body("items.size()", greaterThan(0));
    }

    @Test(priority = 6, dependsOnMethods = "createInvoiceWithProduct")
    public void testDeleteInvoice() {
        given()
                .header("Authorization", TOKEN)
                .pathParam("id", createdInvoiceId)
                .when()
                .delete(BASE_URL + "/{id}")
                .then()
                .statusCode(204);

        // Verify deletion
        given()
                .header("Authorization", TOKEN)
                .pathParam("id", createdInvoiceId)
                .when()
                .get(BASE_URL + "/{id}")
                .then()
                .statusCode(404);
    }
}


package com.example.demo.integration;

import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class InvoiceApiTempTest{

    private static final String BASE_URL = "http://localhost:8080/api/invoices";
    private static final String TOKEN = "Bearer dummy-token";

    private static Long createdInvoiceId;
    private static Long createdProductId;

    @BeforeClass
    public void setup() {
        baseURI = "http://localhost:8080";
    }

    @Test
    public void dummyTest() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL)
                .then()
                .statusCode(anyOf(is(200), is(401), is(403), is(404)));
    }
}

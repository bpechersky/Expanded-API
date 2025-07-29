package com.example.demo.controller;

import com.example.demo.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
public class ProductControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ProductController productController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeClass
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(productController);
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product product = new Product();
        product.setName("TestNG Product");
        product.setPrice(29.99);

        RestAssuredMockMvc
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(objectMapper.writeValueAsString(product))
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .body("name", equalTo("TestNG Product"))
                .body("price", equalTo(29.99f));
    }
}


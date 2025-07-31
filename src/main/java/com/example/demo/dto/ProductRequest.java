package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product update request payload")
public class ProductRequest {

    @Schema(description = "Product name", example = "Updated Product")
    private String name;

    @Schema(description = "Product price", example = "99.99")
    private double price;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Product update request payload")
public class ProductRequest {




    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Schema(description = "Product price", example = "99.99")
    private double price;

    @NotBlank(message = "Name must not be blank")
    @Schema(description = "Product name", example = "Updated Product")
    private String name;



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

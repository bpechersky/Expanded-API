package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Invoice item payload")
public class InvoiceItemRequest {

    @Schema(description = "ID of the product being billed", example = "1", required = true)
    @NotNull(message = "Product ID is required")
    private Long productId;

    @Schema(description = "Quantity of the product", example = "2", required = true)
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @Schema(description = "Unit price of the product", example = "19", required = true)
    @DecimalMin(value = "0.01", message = "Unit price must be at least 0.01")
    private double unitPrice;

    @Schema(description = "Optional description for the item", example = "Replacement part", required = false)
    private String description;

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

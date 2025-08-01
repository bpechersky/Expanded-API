package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
}

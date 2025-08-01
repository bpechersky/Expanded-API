package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Invoice creation payload")
public class InvoiceRequest {

    @Schema(description = "Name of the customer", example = "Alice Johnson", required = true)
    @NotBlank(message = "Customer name must not be blank")
    private String customerName;

    @Schema(description = "Invoice items including product and quantity", required = true)
    @NotNull(message = "Invoice must have items")
    private List<InvoiceItemRequest> items;

    // Getters and Setters

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<InvoiceItemRequest> getItems() {
        return items;
    }

    public void setItems(List<InvoiceItemRequest> items) {
        this.items = items;
    }
}

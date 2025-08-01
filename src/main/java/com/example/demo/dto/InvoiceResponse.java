package com.example.demo.dto;

import com.example.demo.model.Invoice;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Invoice response payload")
public class InvoiceResponse {

    @Schema(description = "ID of the invoice", example = "1")
    private Long id;

    @Schema(description = "Customer name on the invoice", example = "John Doe")
    private String customerName;

    @Schema(description = "Total invoice amount", example = "199.99")
    private double amount;

    public InvoiceResponse(Invoice invoice) {
        this.id = invoice.getId();
        this.customerName = invoice.getCustomerName();
        this.amount = invoice.getAmount();
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getAmount() {
        return amount;
    }
}

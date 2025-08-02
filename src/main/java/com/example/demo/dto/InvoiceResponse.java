package com.example.demo.dto;

import com.example.demo.model.Invoice;
import com.example.demo.model.InvoiceItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.util.List;
@Data
@Schema(description = "Invoice response payload")
public class InvoiceResponse {

    @Getter
    @Schema(description = "ID of the invoice", example = "1")
    private Long id;

    @Getter
    @Schema(description = "Customer name on the invoice", example = "John Doe")
    private String customerName;

    @Getter
    @Schema(description = "Total invoice amount", example = "199.99")
    private double amount;

    private List<InvoiceItem> items;


    public InvoiceResponse(Invoice invoice) {
        this.id = invoice.getId();
        this.customerName = invoice.getCustomerName();
        this.amount = invoice.getAmount();
        this.items = invoice.getItems();
    }

}

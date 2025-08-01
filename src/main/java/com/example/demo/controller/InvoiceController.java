package com.example.demo.controller;

import com.example.demo.dto.InvoiceItemRequest;
import com.example.demo.dto.InvoiceRequest;
import com.example.demo.dto.InvoiceResponse;
import com.example.demo.model.Invoice;
import com.example.demo.model.InvoiceItem;
import com.example.demo.model.Product;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        List<InvoiceItem> items = new ArrayList<>();
        double total = 0;

        for (InvoiceItemRequest itemRequest : request.getItems()) {
            Optional<Product> productOpt = productRepository.findById(itemRequest.getProductId());
            if (productOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Product with ID " + itemRequest.getProductId() + " not found.");
            }

            InvoiceItem item = new InvoiceItem();
            item.setProduct(productOpt.get());
            item.setDescription(itemRequest.getDescription());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());
            items.add(item);

            total += itemRequest.getQuantity() * itemRequest.getUnitPrice();
        }

        Invoice invoice = new Invoice();
        invoice.setCustomerName(request.getCustomerName());
        invoice.setAmount(total);
        invoice.setItems(items);
        items.forEach(i -> i.setInvoice(invoice));

        Invoice saved = invoiceRepository.save(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(new InvoiceResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable Long id, @Valid @RequestBody InvoiceRequest request) {
        Optional<Invoice> existingOpt = invoiceRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Invoice invoice = existingOpt.get();
        List<InvoiceItem> updatedItems = new ArrayList<>();
        double total = 0;

        for (InvoiceItemRequest itemRequest : request.getItems()) {
            Optional<Product> productOpt = productRepository.findById(itemRequest.getProductId());
            if (productOpt.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Product with ID " + itemRequest.getProductId() + " not found.");
            }

            InvoiceItem item = new InvoiceItem();
            item.setProduct(productOpt.get());
            item.setDescription(itemRequest.getDescription());
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(itemRequest.getUnitPrice());
            item.setInvoice(invoice);
            updatedItems.add(item);

            total += item.getQuantity() * item.getUnitPrice();
        }

        invoice.setCustomerName(request.getCustomerName());
        invoice.getItems().clear();
        invoice.getItems().addAll(updatedItems);
        invoice.setAmount(total);

        Invoice saved = invoiceRepository.save(invoice);
        return ResponseEntity.ok(new InvoiceResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(existing -> {
                    invoiceRepository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

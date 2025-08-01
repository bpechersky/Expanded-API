package com.example.demo.controller;

import com.example.demo.dto.InvoiceRequest;
import com.example.demo.dto.InvoiceResponse;
import com.example.demo.model.Invoice;
import com.example.demo.repository.InvoiceRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping
    public List<Invoice> getAll() {
        return invoiceRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@Valid @RequestBody InvoiceRequest request) {
        Invoice invoice = new Invoice();
        invoice.setCustomerName(request.getCustomerName());
        invoice.setAmount(request.getAmount());

        Invoice saved = invoiceRepository.save(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(new InvoiceResponse(saved));
    }



    // ✅ Return 404 if not found
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Return 404 if trying to update non-existent invoice
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> update(@PathVariable Long id, @RequestBody Invoice obj) {
        return invoiceRepository.findById(id)
                .map(existing -> {
                    obj.setId(id);
                    Invoice updated = invoiceRepository.save(obj);
                    return ResponseEntity.status(HttpStatus.CREATED).body(updated); // <-- return 201
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // ✅ Return 404 if trying to delete non-existent invoice
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return invoiceRepository.findById(id)
                .map(existing -> {
                    invoiceRepository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build(); // Explicit type to avoid IntelliJ warning
                })
                .orElse(ResponseEntity.notFound().build());
    }


}

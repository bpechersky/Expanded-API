package com.example.demo.controller;

import com.example.demo.model.Invoice;
import com.example.demo.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository repository;

    @GetMapping
    public List<Invoice> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Invoice> create(@RequestBody Invoice invoice) {
        Invoice saved = repository.save(invoice);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    // ✅ Return 404 if not found
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Return 404 if trying to update non-existent invoice
    @PutMapping("/{id}")
    public ResponseEntity<Invoice> update(@PathVariable Long id, @RequestBody Invoice obj) {
        return repository.findById(id)
                .map(existing -> {
                    obj.setId(id);
                    Invoice updated = repository.save(obj);
                    return ResponseEntity.status(HttpStatus.CREATED).body(updated); // <-- return 201
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // ✅ Return 404 if trying to delete non-existent invoice
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repository.findById(id)
                .map(existing -> {
                    repository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build(); // Explicit type to avoid IntelliJ warning
                })
                .orElse(ResponseEntity.notFound().build());
    }


}

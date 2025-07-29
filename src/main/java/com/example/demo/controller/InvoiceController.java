package com.example.demo.controller;

import com.example.demo.model.Invoice;
import com.example.demo.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Invoice create(@RequestBody Invoice obj) {
        return repository.save(obj);
    }

    @GetMapping("/{id}")
    public Invoice getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Invoice update(@PathVariable Long id, @RequestBody Invoice obj) {
        obj.setId(id);
        return repository.save(obj);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

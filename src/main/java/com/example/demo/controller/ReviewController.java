package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository repository;

    @GetMapping
    public List<Review> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public Review create(@RequestBody Review obj) {
        return repository.save(obj);
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Review update(@PathVariable Long id, @RequestBody Review obj) {
        obj.setId(id);
        return repository.save(obj);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

package com.example.demo.controller;

import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ReviewRepository reviewRepository;



    @GetMapping
    public List<Review> getAll() {
        return reviewRepository.findAll();
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestBody Review review) {
        boolean exists = productRepository.existsById(review.getProduct().getId());
        if (!exists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Product with id " + review.getProduct().getId() + " does not exist.");
        }

        Review saved = reviewRepository.save(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Review> getById(@PathVariable Long id) {
        return reviewRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<Review> update(@PathVariable Long id, @RequestBody Review obj) {
        return reviewRepository.findById(id)
                .map(existing -> {
                    obj.setId(id);
                    return ResponseEntity.ok(reviewRepository.save(obj));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return reviewRepository.findById(id)
                .<ResponseEntity<Void>>map(existing -> {
                    reviewRepository.deleteById(id);
                    return ResponseEntity.noContent().build(); // 204
                })
                .orElse(ResponseEntity.notFound().build());   // 404
    }


}

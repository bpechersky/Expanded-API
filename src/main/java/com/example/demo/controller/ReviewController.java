package com.example.demo.controller;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<?> create(@RequestBody ReviewRequest request) {
        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Product with id " + request.getProductId() + " does not exist."));
        }

        Review review = new Review();
        review.setProduct(productOpt.get());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

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
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewRequest request) {
        return reviewRepository.findById(id)
                .map(existing -> {
                    // Optional: validate product exists if needed
                    Optional<Product> productOpt = productRepository.findById(request.getProductId());
                    if (productOpt.isEmpty()) {
                        return ResponseEntity.badRequest()
                                .body("Product with id " + request.getProductId() + " does not exist.");
                    }

                    existing.setProduct(productOpt.get());
                    existing.setRating(request.getRating());
                    existing.setComment(request.getComment());

                    Review updated = reviewRepository.save(existing);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
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

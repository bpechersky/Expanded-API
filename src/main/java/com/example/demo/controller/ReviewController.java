package com.example.demo.controller;

import com.example.demo.dto.ReviewRequest;
import com.example.demo.dto.ReviewResponse;
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
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewResponse(saved));
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return reviewRepository.findById(id)
                .map(review -> ResponseEntity.ok(new ReviewResponse(review)))
                .orElse(ResponseEntity.notFound().build());
    }




    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewRequest request) {
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if (optionalReview.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Review not found"));
        }

        Optional<Product> optionalProduct = productRepository.findById(request.getProductId());
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Product not found"));
        }

        Review review = optionalReview.get();
        review.setProduct(optionalProduct.get());
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review updatedReview = reviewRepository.save(review);
        return ResponseEntity.ok(new ReviewResponse(updatedReview));
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

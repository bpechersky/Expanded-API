package com.example.demo.dto;

import com.example.demo.model.Review;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Review response payload")
public class ReviewResponse {

    @Schema(description = "ID of the review", example = "1")
    private Long id;

    @Schema(description = "ID of the product being reviewed", example = "101")
    private Long productId;

    @Schema(description = "Rating from 1 to 5", example = "4")
    private int rating;

    @Schema(description = "Comment left by the reviewer", example = "Great product!")
    private String comment;

    // ✅ Constructor used to map entity to DTO
    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.productId = review.getProduct() != null ? review.getProduct().getId() : null;
        this.rating = review.getRating();
        this.comment = review.getComment();
    }

    // ✅ Getters for serialization
    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}

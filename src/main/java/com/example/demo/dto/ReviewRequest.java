package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Payload for creating a new review")
public class ReviewRequest {

    @Schema(description = "ID of the product being reviewed", example = "1", required = true)
    private Long productId;

    @Schema(description = "Rating given to the product (1 to 5)", example = "5", required = true)
    private int rating;

    @Schema(description = "Optional comment for the review", example = "Excellent product!", required = false)
    private String comment;

    // Getters and setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

package com.edu.springboot.dto;

public class TripReviewDto {
    private String review;
    private double rating;

    // Getters & Setters
    public String getReview() {
        return review;
    }
    public void setReview(String review) {
        this.review = review;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
}

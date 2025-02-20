package com.edu.springboot.dto;

public class TripResponseDto {
    private int tripId;
    private int userId;
    private String tripTitle;
    private String startDate;
    private String endDate;
    private String status;
    private String review;
    private double rating;
    private String image;  // TRIP_REVIEW 테이블의 IMAGE 컬럼

    // Getters & Setters
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTripTitle() { return tripTitle; }
    public void setTripTitle(String tripTitle) { this.tripTitle = tripTitle; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getReview() { return review; }
    public void setReview(String review) { this.review = review; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}

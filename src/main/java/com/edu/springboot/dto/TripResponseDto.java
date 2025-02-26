package com.edu.springboot.dto;

import java.util.List;
import java.util.Map;

public class TripResponseDto {
    private int tripId;
    private int userId;
    private String tripTitle;
    private String startDate;
    private String endDate;
    private String status;
    private String review;
    private double rating;
    private String image;
    
    // 일정 관련
    private Map<String, List<String>> itinerary;
    private List<String> itineraryDates;
    
    // 동행자(참가자) 닉네임 목록
    private List<String> participantNames;
    
    // 방장(여행 생성자) 닉네임 추가
    private String creatorNickname;

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

    public Map<String, List<String>> getItinerary() { return itinerary; }
    public void setItinerary(Map<String, List<String>> itinerary) { this.itinerary = itinerary; }
    public List<String> getItineraryDates() { return itineraryDates; }
    public void setItineraryDates(List<String> itineraryDates) { this.itineraryDates = itineraryDates; }
    
    public List<String> getParticipantNames() { return participantNames; }
    public void setParticipantNames(List<String> participantNames) { this.participantNames = participantNames; }
    
    public String getCreatorNickname() { return creatorNickname; }
    public void setCreatorNickname(String creatorNickname) { this.creatorNickname = creatorNickname; }
}

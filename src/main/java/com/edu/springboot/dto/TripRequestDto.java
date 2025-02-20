package com.edu.springboot.dto;

public class TripRequestDto {
    private int tripId; // DB에서 auto-generated (시퀀스 사용)
    private int userId; // 로그인된 사용자 ID
    private String title; // 여행 제목
    private String startDate; // "YYYY-MM-DD" 형식
    private String endDate;   // "YYYY-MM-DD" 형식

    // Getters & Setters
    public int getTripId() {
        return tripId;
    }
    public void setTripId(int tripId) {
        this.tripId = tripId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}

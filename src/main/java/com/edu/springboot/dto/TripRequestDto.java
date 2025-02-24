package com.edu.springboot.dto;

public class TripRequestDto {
    private int tripId;     // DB에서 시퀀스로 생성
    private int userId;     // 로그인 사용자 ID
    private String title;   // 여행 제목
    private String startDate; // "YYYY-MM-DD"
    private String endDate;   // "YYYY-MM-DD"

    // Getters & Setters
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
}

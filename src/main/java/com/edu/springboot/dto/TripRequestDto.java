package com.edu.springboot.dto;

public class TripRequestDto {
    private int tripId;      // DB auto-generated (시퀀스 사용)
    private int userId;      // 여행 생성자 ID
    private String title;    // 여행 제목
    private String startDate; // 시작 날짜 ("YYYY-MM-DD")
    private String endDate;   // 종료 날짜 ("YYYY-MM-DD")

    // 기본 생성자
    public TripRequestDto() {
    }

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

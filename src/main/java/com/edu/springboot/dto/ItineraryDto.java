package com.edu.springboot.dto;

public class ItineraryDto {
    private int itineraryId;
    private int tripId;
    private String itineraryDate; // "YYYY-MM-DD" 형식
    private String placeName;     // 관광지 이름
    private String contentId;     // 관광지 API의 콘텐츠 ID (필요시)
    private int seq;

    // Getters & Setters
    public int getItineraryId() { return itineraryId; }
    public void setItineraryId(int itineraryId) { this.itineraryId = itineraryId; }
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    public String getItineraryDate() { return itineraryDate; }
    public void setItineraryDate(String itineraryDate) { this.itineraryDate = itineraryDate; }
    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }
    public String getContentId() { return contentId; }
    public void setContentId(String contentId) { this.contentId = contentId; }
    public int getSeq() { return seq; }
    public void setSeq(int seq) { this.seq = seq; }
}

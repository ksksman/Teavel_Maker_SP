package com.edu.springboot.dto;

public class ItineraryDto {
    private int itineraryId;
    private int tripId;
    private String itineraryDate;
    private int seq;
    private String placeName;

    // Getters & Setters
    public int getItineraryId() { return itineraryId; }
    public void setItineraryId(int itineraryId) { this.itineraryId = itineraryId; }
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    public String getItineraryDate() { return itineraryDate; }
    public void setItineraryDate(String itineraryDate) { this.itineraryDate = itineraryDate; }
    public int getSeq() { return seq; }
    public void setSeq(int seq) { this.seq = seq; }
    public String getPlaceName() { return placeName; }
    public void setPlaceName(String placeName) { this.placeName = placeName; }
}

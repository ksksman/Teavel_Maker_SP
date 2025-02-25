package com.edu.springboot.dto;

import java.time.LocalDate;

public class TripItineraryDTO {
    private Long itineraryId;
    private Long tripId;
    private LocalDate itineraryDate;
    private String placeName;
    private Integer seq;
    private String contentId; //  contentId 유지
    private Double lat; //  위도 추가
    private Double lng; //  경도 추가

    public Long getItineraryId() {
        return itineraryId;
    }

    public void setItineraryId(Long itineraryId) {
        this.itineraryId = itineraryId;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public LocalDate getItineraryDate() {
        return itineraryDate;
    }

    public void setItineraryDate(LocalDate itineraryDate) {
        this.itineraryDate = itineraryDate;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}

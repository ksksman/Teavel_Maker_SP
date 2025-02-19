package com.edu.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TravelPointDto {
    private Long tripId;
    private String contentId;
    private String placeName;
    private String itineraryDate; // YYYY-MM-DD 형식
    private int sequence;
}

package com.edu.springboot.dto;

import lombok.Data;
import java.util.List;

@Data
public class MyTripDto {
    private Long tripId;
    private String tripTitle;
    private String startDate;
    private String endDate;
    private Long userId;
    

    @Data
    public static class ParticipantDto {
    	private Long tripId;
        private Long userId;
        private String nickname;
        private String role;
    }
}

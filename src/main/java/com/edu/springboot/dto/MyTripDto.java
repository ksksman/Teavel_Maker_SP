package com.edu.springboot.dto;

import lombok.Data;
import java.util.List;

@Data
public class MyTripDto {
    private Long trip_id;
    private String trip_Title;
    private String startDate;
    private String endDate;
    private Long user_Id;

	@Data
    public static class ParticipantDto {
    	private Long trip_Id;
        private Long user_Id;
        private String nickname;
        private String role;
    }
}
package com.edu.springboot.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantDto {
    private Long trip_Id; // ✅ tripId 추가
    private Long user_Id;
    private String nickname;
    private String role;
}
package com.edu.springboot.jdbc;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberDTO {
    private Integer userId;
    private String email;
    private String password;
    private String nickname;
    private LocalDate birthdate;
    private String gender;
    private String phoneNumber;
    private String marketingConsent = "0"; // 🚀 기본값을 "0"으로 설정하여 NULL 방지
    private String recommendedFriend = ""; // 🚀 NULL 대신 빈 문자열로 기본값 설정
    private java.sql.Timestamp accountCreatedAt;
    private java.sql.Date updatedAt;

    // 기본 생성자
    public MemberDTO() {}

    // 이메일만 받는 생성자
    public MemberDTO(String email) {
        this.email = email;
    }
}

package com.edu.springboot.jdbc;

import lombok.Data;

@Data
public class MemberDTO {
    private Integer userId;
    private String email;
    private String password;
    private String nickname;
    private Integer age;
    private String gender;
    private String phoneNumber;
    private String marketingConsent;
    private String recommendedFriend;
    private java.sql.Timestamp accountCreatedAt;
    private java.sql.Date updatedAt;

    // ✅ 기본 생성자 (MyBatis 및 JSON 파싱을 위해 필요)
    public MemberDTO() {}

    // ✅ 이메일만 받는 생성자 추가 (회원 조회용)
    public MemberDTO(String email) {
        this.email = email;
    }
}


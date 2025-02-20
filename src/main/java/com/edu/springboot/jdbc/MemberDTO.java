package com.edu.springboot.jdbc;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberDTO {
    private Integer user_Id;
    private String email;
    private String password;
    private String nickname;
    private LocalDate birthdate;
    private String gender;
    private String phone_Number;
    private String marketing_Consent = "0"; // 🚀 기본값을 "0"으로 설정하여 NULL 방지
    private String recommended_Friend = ""; // 🚀 NULL 대신 빈 문자열로 기본값 설정
    private java.sql.Timestamp account_Created_At;
    private java.sql.Date updated_At;

    // 기본 생성자
    public MemberDTO() {}

    // 이메일만 받는 생성자
    public MemberDTO(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phone_Number;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phone_Number = phoneNumber;
    }
}

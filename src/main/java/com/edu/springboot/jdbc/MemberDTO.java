package com.edu.springboot.jdbc;

import lombok.Data;
import java.time.LocalDate;

@Data
public class MemberDTO {
    private Integer user_Id;
    private String email;
    private String password;
    private String nickname;

    private LocalDate birthdate = LocalDate.of(2000, 1, 1); // 기본값 설정
    private String gender = "M"; // ✅ 기본값을 'M'으로 변경 (제약조건 위배 방지)
    private String phoneNumber = "000-0000-0000"; // 기본값 설정
    private String marketingConsent = "0"; // 기본값 설정
    private java.sql.Timestamp accountCreatedAt;
    private java.sql.Date updatedAt;

    // 기본 생성자
    public MemberDTO() {}

    // 이메일만 받는 생성자
    public MemberDTO(String email) {
        this.email = email;
    }
    
    //임시추가 (
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    //여기까지 )
    public Integer getUserId() {
        return user_Id;
    }

    public void setUserId(Integer userId) {
        this.user_Id = userId;
    }

}

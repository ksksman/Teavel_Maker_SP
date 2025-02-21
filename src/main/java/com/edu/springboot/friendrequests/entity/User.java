package com.edu.springboot.friendrequests.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users") // ✅ 테이블명 소문자로 통일
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ IDENTITY 사용
    @Column(name = "user_id") // ✅ 컬럼명을 user_id로 변경
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nickname;
    private String email;

    @Column(name = "phone_number") // ✅ DB 컬럼명과 통일
    private String phoneNumber;

    @Column(name = "gender") 
    private String gender;

    @Column(name = "birthdate") // ✅ LocalDate로 변경 (DB DATE와 매핑)
    private LocalDate birthdate;
}

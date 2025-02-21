package com.edu.springboot.friendrequests.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "friendrequests") // ✅ 테이블명 소문자로 통일
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id") // ✅ 컬럼명을 request_id로 변경
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false) // ✅ 컬럼명 user_id 사용
    private User requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false) // ✅ 컬럼명 user_id 사용
    private User receiver;

    @Column(nullable = false)
    private String status; // PENDING, ACCEPTED, REJECTED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(); // ✅ 생성 시간 자동 설정
    }
}

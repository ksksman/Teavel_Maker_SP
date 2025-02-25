package com.edu.springboot.friendrequests.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "friendrequests") // ✅ 테이블명과 일치하도록 변경
public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id") // ✅ 테이블 컬럼명과 맞추기
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false) // ✅ 테이블의 컬럼명과 매핑
    private User requester;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Column(nullable = false)
    private String status = "PENDING"; // ✅ 기본 상태 설정


    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // ✅ `getUserId()` 메서드 사용
    public Long getRequesterUserId() {
        return requester.getUserId();
    }

    public Long getReceiverUserId() {
        return receiver.getUserId();
    }
}

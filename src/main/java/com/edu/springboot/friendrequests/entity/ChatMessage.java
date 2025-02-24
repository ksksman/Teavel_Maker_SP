package com.edu.springboot.friendrequests.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ✅ 기본 키 설정
    private Long id;

    private Long sender;   // 보낸 사람 ID
    private Long receiver; // 받는 사람 ID
    private String content;
    
    @Enumerated(EnumType.STRING)
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}

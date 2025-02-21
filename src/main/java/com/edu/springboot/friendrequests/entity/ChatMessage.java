package com.edu.springboot.friendrequests.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String sender;  // 보낸 사람
    private String receiver; // 받는 사람
    private String content; // 메시지 내용
    private MessageType type; // 메시지 유형 (입장, 대화)

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }
}

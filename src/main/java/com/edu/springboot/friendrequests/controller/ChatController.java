package com.edu.springboot.friendrequests.controller;



import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.edu.springboot.friendrequests.entity.ChatMessage;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage") // 사용자가 메시지를 보낼 때
    @SendTo("/topic/public") // 모든 구독자에게 메시지를 보냄
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser") // 사용자가 채팅방에 들어올 때
    @SendTo("/topic/public")
    public ChatMessage addUser(ChatMessage chatMessage) {
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        return chatMessage;
    }
}

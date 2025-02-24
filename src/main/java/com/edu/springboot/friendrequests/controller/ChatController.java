package com.edu.springboot.friendrequests.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.edu.springboot.friendrequests.entity.ChatMessage;

@Controller
public class ChatController {

	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/chat")
	public ChatMessage sendMessage(ChatMessage chatMessage) {
	    if (chatMessage.getSender() == null || chatMessage.getReceiver() == null) {
	        System.out.println("🚨 오류: sender 또는 receiver가 null입니다.");
	    }
	    return chatMessage;
	}


    @MessageMapping("/chat.addUser") // 사용자가 채팅방에 들어올 때
    @SendTo("/topic/chat")
    public ChatMessage addUser(ChatMessage chatMessage) {
        System.out.println("👤 새로운 사용자 입장: " + chatMessage.getSender());
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        return chatMessage;
    }
}

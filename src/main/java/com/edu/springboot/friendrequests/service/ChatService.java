package com.edu.springboot.friendrequests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edu.springboot.friendrequests.entity.ChatMessage;
import com.edu.springboot.friendrequests.repository.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatRepository.save(chatMessage);  // ✅ 메시지를 DB에 저장
    }
}

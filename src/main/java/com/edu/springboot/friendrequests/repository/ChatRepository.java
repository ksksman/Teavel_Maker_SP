package com.edu.springboot.friendrequests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edu.springboot.friendrequests.entity.ChatMessage;

@Repository
public interface ChatRepository extends JpaRepository<ChatMessage, Long> {
}

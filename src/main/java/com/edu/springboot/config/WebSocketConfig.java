package com.edu.springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // ✅ WebSocket 엔드포인트
                .setAllowedOriginPatterns("http://localhost:5173") // ✅ React 개발 서버 도메인 허용
                .withSockJS(); // ✅ SockJS 지원
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // ✅ 메시지를 전송할 경로 설정
        registry.setApplicationDestinationPrefixes("/app"); // ✅ 클라이언트에서 메시지를 보낼 경로 설정
    }
}

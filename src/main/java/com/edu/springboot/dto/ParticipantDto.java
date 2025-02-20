package com.edu.springboot.dto;

public class ParticipantDto {
    private Long userId;
    private String nickname;

    // 기본 생성자
    public ParticipantDto() {}

    // 생성자
    public ParticipantDto(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }

    // Getter & Setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}

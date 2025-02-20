package com.edu.springboot.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserDTO {
    private Long oauthId;
    private Long userId;
    private String providerName;
    private String providerUserId;
    private Timestamp linkedAt;

    // 추가 생성자 (DB 저장 시 linkedAt 자동 설정)
    public OAuthUserDTO(Long userId, String providerName, String providerUserId) {
        this.userId = userId;
        this.providerName = providerName;
        this.providerUserId = providerUserId;
        this.linkedAt = new Timestamp(System.currentTimeMillis()); // 현재 시간 설정
    }
}

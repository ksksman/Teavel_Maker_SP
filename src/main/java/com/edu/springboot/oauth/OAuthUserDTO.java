package com.edu.springboot.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUserDTO {
    private Integer oauthId;          // ✅ Long → Integer (DB와 일관성 유지)
    private Integer userId;           // ✅ Integer로 통일 (User 테이블과 일관성 유지)
    private String providerName;
    private String providerUserId;
    private Timestamp linkedAt;
    private String nickname;

    // ✅ 새로운 OAuth 계정 생성 시 기본값 포함 생성자
    public OAuthUserDTO(Integer userId, String providerName, String providerUserId, String nickname) {
        this.userId = userId;
        this.providerName = providerName;
        this.providerUserId = providerUserId;
        this.nickname = nickname;
        this.linkedAt = new Timestamp(System.currentTimeMillis()); // 🔹 현재 시간 자동 설정
    }
}

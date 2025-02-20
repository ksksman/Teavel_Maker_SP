package com.edu.springboot.oauth;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.core.ParameterizedTypeReference;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthLoginController {

    private static final String KAKAO_CLIENT_ID = "389b95d1ffd38f723c94e788919d6b4d";
    private static final String KAKAO_REDIRECT_URI = "http://localhost:8586/auth/kakao/callback"; // ✅ 변경
    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final WebClient webClient = WebClient.builder().build(); // ✅ WebClient 재사용 가능하도록 변경

    /**
     * 🔹 1️⃣ 카카오 로그인 URL 리턴 (프론트에서 이 URL로 이동)
     */
    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoLoginUrl() {
        String authUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URI;
        return ResponseEntity.ok(Map.of("loginUrl", authUrl));
    }

    /**
     * 🔹 2️⃣ 카카오 콜백 (AccessToken 요청 & 사용자 정보 조회)
     */
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code, HttpSession session) {
        try {
            // 🔹 (1) Access Token 요청
            Map<String, Object> tokenResponse = webClient.post()
                    .uri(KAKAO_TOKEN_URI)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .bodyValue("grant_type=authorization_code"
                            + "&client_id=" + KAKAO_CLIENT_ID
                            + "&redirect_uri=" + KAKAO_REDIRECT_URI
                            + "&code=" + code)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to get access token"));
            }

            String accessToken = (String) tokenResponse.get("access_token");
            session.setAttribute("accessToken", accessToken); // ✅ 세션에 저장

            // 🔹 (2) 카카오 사용자 정보 요청
            Map<String, Object> userInfoResponse = webClient.get()
                    .uri(KAKAO_USER_INFO_URI)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (userInfoResponse == null || !userInfoResponse.containsKey("id")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to get user info"));
            }

            // 🔹 (3) 사용자 정보 추출
            String providerUserId = userInfoResponse.get("id").toString();
            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfoResponse.get("kakao_account");

            String nickname = "Unknown";
            String email = providerUserId + "@kakao.oauth";

            if (kakaoAccount != null) {
                // 닉네임이 존재하는 경우 추출
                if (kakaoAccount.get("profile") != null) {
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                    if (profile.get("nickname") != null) {
                        nickname = profile.get("nickname").toString();
                    }
                }

                // 이메일이 존재하는 경우 추출
                if (kakaoAccount.get("email") != null) {
                    email = kakaoAccount.get("email").toString();
                }
            }

            // ✅ JSON 형태로 클라이언트에 반환 (DB 저장 X)
            Map<String, Object> response = Map.of(
                    "provider", "kakao",
                    "providerUserId", providerUserId,
                    "nickname", nickname,
                    "email", email,
                    "accessToken", accessToken
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Exception occurred: " + e.getMessage()));
        }
    }
}

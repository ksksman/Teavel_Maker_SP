package com.edu.springboot.oauth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
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
    private static final String KAKAO_REDIRECT_URI = "http://localhost:8586/auth/kakao/callback";
    private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final WebClient webClient = WebClient.builder().build();
    private final OAuthUserService oauthUserService;

    /**
     * 🔹 1️⃣ 카카오 로그인 URL 반환
     */
    @GetMapping("/kakao/login")
    public ResponseEntity<?> kakaoLoginUrl() {
        String authUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + KAKAO_CLIENT_ID
                + "&redirect_uri=" + KAKAO_REDIRECT_URI;
        return ResponseEntity.ok(Map.of("loginUrl", authUrl));
    }

    /**
     * 🔹 2️⃣ 카카오 콜백 (AccessToken 요청 & 사용자 정보 조회 & DB 저장)
     */
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code, HttpSession session) {
        try {
            // ✅ (1) Access Token 요청
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", KAKAO_CLIENT_ID);
            params.add("redirect_uri", KAKAO_REDIRECT_URI);
            params.add("code", code);

            Map<String, Object> tokenResponse = webClient.post()
                    .uri(KAKAO_TOKEN_URI)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to get access token"));
            }

            String accessToken = (String) tokenResponse.get("access_token");

            // ✅ (2) 카카오 사용자 정보 요청
            Map<String, Object> userInfoResponse = webClient.get()
                    .uri(KAKAO_USER_INFO_URI)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (userInfoResponse == null || !userInfoResponse.containsKey("id")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to get user info"));
            }

            // ✅ (3) 사용자 정보 추출
            String providerUserId = userInfoResponse.get("id").toString();
            Map<String, Object> kakaoAccount = (Map<String, Object>) userInfoResponse.get("kakao_account");

            String nickname = "Unknown";
            String email = providerUserId + "@kakao.oauth"; // 기본 이메일 값 설정

            if (kakaoAccount != null) {
                if (kakaoAccount.get("profile") != null) {
                    Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                    if (profile.get("nickname") != null) {
                        nickname = profile.get("nickname").toString();
                    }
                }

                if (kakaoAccount.get("email") != null) {
                    email = kakaoAccount.get("email").toString();
                }
            }

            // ✅ (4) DB 저장 또는 조회
            OAuthUserDTO user = oauthUserService.processOAuthLogin("kakao", providerUserId, email, nickname);

            if (user == null) {
                return ResponseEntity.internalServerError().body(Map.of("error", "OAuthUserDTO 저장 후 조회 실패"));
            }

            // ✅ (5) 세션에 저장 후 리디렉트
            session.setAttribute("loginUser", user);
            System.out.println("✅ 세션 저장 완료: " + user);

            // ✅ 프론트에서 존재하는 경로로 변경 (/home)
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:5173/main") // ✅ 프론트엔드 홈 경로로 변경
                    .build();


        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Exception: " + e.getMessage()));
        }
    }

    /**
     * 🔹 3️⃣ 로그인된 사용자 세션 확인 API
     */
    @GetMapping("/me")  // ✅ `/checkSession` 대신 `/me` 사용
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        OAuthUserDTO user = (OAuthUserDTO) session.getAttribute("loginUser");

        if (user != null) {
            return ResponseEntity.ok(user);  // ✅ 로그인된 사용자 정보 반환
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No active session"));
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // ✅ 세션 완전 삭제
        return ResponseEntity.ok(Map.of("message", "로그아웃 완료"));
    }


}

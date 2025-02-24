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

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Map;

@RestController
@RequestMapping("/auth/naver")
@RequiredArgsConstructor
public class NaverLoginController {

    private static final String NAVER_CLIENT_ID = "9gOu_hi6zf7DQpgPhsQr";
    private static final String NAVER_CLIENT_SECRET = "ZZZWYFUl_T";
    private static final String NAVER_REDIRECT_URI = "http://localhost:8586/auth/naver/callback";
    private static final String NAVER_TOKEN_URI = "https://nid.naver.com/oauth2.0/token";
    private static final String NAVER_USER_INFO_URI = "https://openapi.naver.com/v1/nid/me";

    private final WebClient webClient = WebClient.builder().build();
    private final OAuthUserService oauthUserService;

    /**
     * 🔹 1️⃣ 네이버 로그인 URL 반환 (state 값을 세션에 저장)
     */
    @GetMapping("/login")
    public ResponseEntity<?> naverLoginUrl(HttpSession session) {
        String state = new BigInteger(130, new SecureRandom()).toString(32); // ✅ 랜덤 state 값 생성
        session.setAttribute("naver_state", state); // ✅ state 세션에 저장

        String authUrl = "https://nid.naver.com/oauth2.0/authorize?"
                + "client_id=" + NAVER_CLIENT_ID
                + "&redirect_uri=" + NAVER_REDIRECT_URI
                + "&response_type=code"
                + "&state=" + state; // ✅ state 포함

        return ResponseEntity.ok(Map.of("loginUrl", authUrl));
    }

    /**
     * 🔹 2️⃣ 네이버 콜백 (AccessToken 요청 & 사용자 정보 조회 & DB 저장)
     */
    @GetMapping("/callback")
    public ResponseEntity<?> naverCallback(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) {
        try {
            System.out.println("✅ [네이버 로그인] 콜백 호출됨: code = " + code + ", state = " + state);

            // 🔹 세션에서 저장된 state 값 확인
            String storedState = (String) session.getAttribute("naver_state");
            System.out.println("✅ [네이버 로그인] 세션에 저장된 state: " + storedState);

            if (storedState == null || !storedState.equals(state)) {
                System.out.println("🚨 [네이버 로그인] state 값 불일치! 요청된 state = " + state + ", 세션 state = " + storedState);
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid state parameter"));
            }

            // ✅ Access Token 요청
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", NAVER_CLIENT_ID);
            params.add("client_secret", NAVER_CLIENT_SECRET);
            params.add("redirect_uri", NAVER_REDIRECT_URI);
            params.add("code", code);
            params.add("state", state);

            Map<String, Object> tokenResponse = webClient.post()
                    .uri(NAVER_TOKEN_URI)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                    .body(BodyInserters.fromFormData(params))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                System.out.println("🚨 [네이버 로그인] Access Token 요청 실패! 응답: " + tokenResponse);
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to get access token"));
            }

            String accessToken = (String) tokenResponse.get("access_token");
            System.out.println("✅ [네이버 로그인] Access Token 획득 성공: " + accessToken);

            // ✅ 네이버 사용자 정보 요청
            Map<String, Object> userInfoResponse = webClient.get()
                    .uri(NAVER_USER_INFO_URI)
                    .header("Authorization", "Bearer " + accessToken)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (userInfoResponse == null || !userInfoResponse.containsKey("response")) {
                System.out.println("🚨 [네이버 로그인] 사용자 정보 요청 실패! 응답: " + userInfoResponse);
                return ResponseEntity.badRequest().body(Map.of("error", "Failed to get user info"));
            }

            // ✅ 사용자 정보 파싱
            Map<String, Object> response = (Map<String, Object>) userInfoResponse.get("response");
            System.out.println("✅ [네이버 로그인] 네이버 사용자 정보: " + response);

            String providerUserId = response.get("id").toString();
            String email = response.getOrDefault("email", providerUserId + "@naver.oauth").toString();
            String nickname = response.getOrDefault("nickname", "Unknown").toString();

            // ✅ DB 저장 및 조회
            System.out.println("✅ [네이버 로그인] DB 저장 프로세스 시작");
            OAuthUserDTO user = oauthUserService.processOAuthLogin("naver", providerUserId, email, nickname);

            if (user == null) {
                System.out.println("🚨 [네이버 로그인] OAuthUserDTO 저장 후 조회 실패!");
                return ResponseEntity.internalServerError().body(Map.of("error", "OAuthUserDTO 저장 후 조회 실패"));
            }

            // ✅ 세션에 저장
            session.setAttribute("loginUser", user);
            System.out.println("✅ [네이버 로그인] DB 저장 완료! 사용자 정보: " + user);

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header(HttpHeaders.LOCATION, "http://localhost:5173/main")
                    .build();
        } catch (Exception e) {
            System.out.println("🚨 [네이버 로그인] 예외 발생: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", "Exception: " + e.getMessage()));
        }
    }

    /**
     * 🔹 3️⃣ 로그인된 사용자 정보 확인 API
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        OAuthUserDTO user = (OAuthUserDTO) session.getAttribute("loginUser");

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "No active session"));
        }
    }

    /**
     * 🔹 4️⃣ 네이버 로그아웃 API
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("message", "네이버 로그아웃 완료"));
    }
}
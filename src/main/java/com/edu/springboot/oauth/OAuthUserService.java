package com.edu.springboot.oauth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.edu.springboot.jdbc.MemberDTO;
import com.edu.springboot.jdbc.IMemberDAO;

@Service
@RequiredArgsConstructor
public class OAuthUserService {

    private final OAuthUserDAO oauthUserDAO;
    private final IMemberDAO memberDAO;

    @Transactional
    public OAuthUserDTO processOAuthLogin(String providerName, String providerUserId, String email, String nickname) {
        System.out.println("🔹 SNS 로그인 요청: " + providerName + ", ProviderUserID: " + providerUserId);

        // 1️⃣ 기존 OAuth 사용자 조회
        OAuthUserDTO existingUser = oauthUserDAO.findByProviderAndProviderUserId(providerName, providerUserId);
        if (existingUser != null) {
            System.out.println("✅ 기존 OAuth 사용자 확인: " + existingUser);
            return existingUser;
        }

        // 2️⃣ USERS 테이블에서 user_id 조회
        Integer userId = oauthUserDAO.findUserIdByEmail(email);
        System.out.println("🔹 기존 회원 조회 결과: " + userId);

        // 3️⃣ 기존 회원이 없으면 USERS 테이블에 새로운 회원 추가
        if (userId == null) {
            System.out.println("🚀 새로운 회원 생성 중... (Email: " + email + ")");

            // ✅ 닉네임 중복 검사 후 변경
            String finalNickname = generateUniqueNickname(nickname);

            MemberDTO newUser = new MemberDTO();
            newUser.setEmail(email);
            newUser.setNickname(finalNickname);
            newUser.setPassword("OAUTH_USER");
            newUser.setBirthdate(null);
            newUser.setGender("M");
            newUser.setPhoneNumber("000-0000-0000");
            newUser.setMarketingConsent("0");

            try {
                memberDAO.createUser(newUser);
                userId = newUser.getUser_Id();  // 자동 생성된 user_id 가져오기
                if (userId == null) {
                    throw new RuntimeException("🚨 회원 정보 저장 후 userId 조회 실패 (Email: " + email + ")");
                }
                System.out.println("✅ 새로운 회원 저장 완료 (UserID: " + userId + ")");
            } catch (Exception e) {
                throw new RuntimeException("🚨 USERS 테이블 저장 실패: " + e.getMessage());
            }
        }

        // 4️⃣ USER_OAUTH 테이블에 OAuth 정보 저장
        OAuthUserDTO newOAuthUser = new OAuthUserDTO(userId, providerName, providerUserId, nickname);
        try {
            oauthUserDAO.saveOAuthUser(newOAuthUser);
            System.out.println("✅ USER_OAUTH 테이블 저장 완료: " + newOAuthUser);
        } catch (Exception e) {
            throw new RuntimeException("🚨 USER_OAUTH 저장 실패: " + e.getMessage());
        }

        // ✅ 저장 후 최종 데이터 조회 확인
        OAuthUserDTO savedUser = oauthUserDAO.findByProviderAndProviderUserId(providerName, providerUserId);
        System.out.println("✅ 최종 조회된 OAuth 데이터: " + savedUser);

        if (savedUser == null) {
            throw new RuntimeException("🚨 저장 후 OAuthUserDTO 조회 실패!");
        }

        return savedUser;
    }

    /**
     * ✅ 닉네임 중복 검사 후, 중복되면 숫자 붙이기
     */
    private String generateUniqueNickname(String baseNickname) {
        String newNickname = baseNickname;
        int count = 1;

        // 닉네임이 이미 존재하면 뒤에 숫자를 붙임
        while (oauthUserDAO.findUserIdByNickname(newNickname) != null) {
            newNickname = baseNickname + count;
            count++;
        }

        System.out.println("✅ 생성된 최종 닉네임: " + newNickname);
        return newNickname;
    }
}


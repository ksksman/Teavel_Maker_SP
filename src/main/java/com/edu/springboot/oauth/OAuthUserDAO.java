package com.edu.springboot.oauth;

import org.apache.ibatis.annotations.*;

import com.edu.springboot.jdbc.MemberDTO;

@Mapper
public interface OAuthUserDAO {

    // ✅ 기존 OAuth 정보 조회 (providerName과 providerUserId 기준)
    @Select("SELECT * FROM User_OAuth WHERE provider_name = #{providerName} AND provider_user_id = #{providerUserId}")
    OAuthUserDTO findByProviderAndProviderUserId(@Param("providerName") String providerName, @Param("providerUserId") String providerUserId);

    // ✅ USERS 테이블에서 user_id 조회 (이메일 기준)
    @Select("SELECT user_id FROM Users WHERE email = #{email}")
    Long findUserIdByEmail(@Param("email") String email);

    // ✅ USERS 테이블에 새 유저 추가 (user_id 자동 증가)
    @Insert("INSERT INTO Users (email, password, nickname, birthdate, gender, phone_number, marketing_consent, recommended_friend, updated_at) " +
            "VALUES (#{email}, 'OAUTH_USER', #{nickname}, #{birthdate}, #{gender}, #{phoneNumber}, #{marketingConsent}, #{recommendedFriend}, CURRENT_DATE)")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void createUser(MemberDTO user);

    // ✅ USER_OAUTH 테이블에 OAuth 정보 추가
    @Insert("INSERT INTO User_OAuth (oauth_id, user_id, provider_name, provider_user_id, linked_at) " +
            "VALUES (oauth_seq.NEXTVAL, #{userId}, #{providerName}, #{providerUserId}, CURRENT_TIMESTAMP)")
    void saveOAuthUser(OAuthUserDTO oauthUser);
}

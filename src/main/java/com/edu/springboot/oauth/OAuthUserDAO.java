package com.edu.springboot.oauth;

import org.apache.ibatis.annotations.*;
import com.edu.springboot.jdbc.MemberDTO;

@Mapper
public interface OAuthUserDAO {

    // ✅ 기존 OAuth 사용자 조회 (OAuthUserDTO 매핑)
    @Select("SELECT OAUTH_ID, USER_ID, PROVIDER_NAME, PROVIDER_USER_ID, LINKED_AT, NICKNAME FROM USER_OAUTH " +
            "WHERE PROVIDER_NAME = #{providerName} AND PROVIDER_USER_ID = #{providerUserId}")
    @Results(id = "OAuthUserResultMap", value = {
        @Result(column = "OAUTH_ID", property = "oauthId"),
        @Result(column = "USER_ID", property = "user_Id"),
        @Result(column = "PROVIDER_NAME", property = "providerName"),
        @Result(column = "PROVIDER_USER_ID", property = "providerUserId"),
        @Result(column = "LINKED_AT", property = "linkedAt"),
        @Result(column = "NICKNAME", property = "nickname")
    })
    OAuthUserDTO findByProviderAndProviderUserId(@Param("providerName") String providerName, @Param("providerUserId") String providerUserId);

    // ✅ USERS 테이블에서 user_id 조회 (이메일 기준, 존재하면 가져오기)
    @Select("SELECT user_id FROM USERS WHERE email = #{email}")
    Integer findUserIdByEmail(@Param("email") String email);

    // ✅ 닉네임 중복 확인 (닉네임이 존재하면 user_id 반환)
    @Select("SELECT user_id FROM USERS WHERE nickname = #{nickname}")
    Integer findUserIdByNickname(@Param("nickname") String nickname);

    // ✅ USERS 테이블에 SNS 회원 추가 (닉네임 중복 방지 적용)
    @Insert("INSERT INTO USERS (email, password, nickname, birthdate, gender, phone_number, marketing_consent, updated_at) " +
            "VALUES (#{email}, 'OAUTH_USER', #{nickname}, TO_DATE('2000-01-01', 'YYYY-MM-DD'), 'M', '000-0000-0000', '0', CURRENT_DATE)")
    @Options(useGeneratedKeys = true, keyProperty = "user_Id")
    int createUser(MemberDTO user);

    // ✅ USER_OAUTH 테이블에 OAuth 정보 추가
    @Insert("INSERT INTO USER_OAUTH (OAUTH_ID, USER_ID, PROVIDER_NAME, PROVIDER_USER_ID, LINKED_AT, NICKNAME) " +
            "VALUES (oauth_seq.NEXTVAL, #{user_Id}, #{providerName}, #{providerUserId}, CURRENT_TIMESTAMP, #{nickname})")
    void saveOAuthUser(OAuthUserDTO oauthUser);

    // ✅ USER_OAUTH 테이블 닉네임 업데이트 (닉네임이 변경된 경우)
    @Update("UPDATE USER_OAUTH SET NICKNAME = #{nickname} WHERE PROVIDER_USER_ID = #{providerUserId}")
    void updateNickname(@Param("nickname") String nickname, @Param("providerUserId") String providerUserId);
}


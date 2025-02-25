package com.edu.springboot.jdbc;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IMemberDAO {
    List<MemberDTO> selectAll();
    MemberDTO selectByEmail(@Param("email") String email);
    int insert(MemberDTO memberDTO);
    int update(MemberDTO memberDTO);
    int delete(@Param("email") String email);
    MemberDTO login(@Param("email") String email);

    // ✅ 닉네임으로 회원 조회
    MemberDTO selectByNickname(@Param("nickname") String nickname);

    // ✅ 비밀번호 변경
    int updatePassword(@Param("email") String email, @Param("password") String password);

    // ✅ SNS 로그인 시 기본값을 설정하여 회원 가입
    @Insert("INSERT INTO USERS (EMAIL, PASSWORD, NICKNAME, BIRTHDATE, GENDER, PHONE_NUMBER, MARKETING_CONSENT, UPDATED_AT) " +
            "VALUES (#{email}, 'OAUTH_USER', #{nickname}, TO_DATE('2000-01-01', 'YYYY-MM-DD'), 'M', '000-0000-0000', '0', CURRENT_DATE)")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "USER_ID") // 자동 증가된 USER_ID 반환
    void createUser(MemberDTO user);
	Integer findUserIdByEmail(String email);

    // ✅ 비밀번호 찾기 - 인증번호 저장
    int updateVerificationCode(@Param("email") String email, @Param("code") String code);

    // ✅ 비밀번호 찾기 - 인증번호 조회
    String getVerificationCode(@Param("email") String email);
    
    @Select("SELECT * FROM USERS WHERE USER_ID = #{userId}")
    MemberDTO selectById(@Param("userId") Long userId);

    
}

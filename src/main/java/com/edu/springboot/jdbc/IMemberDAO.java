package com.edu.springboot.jdbc;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    // ✅ 비밀번호 찾기 - 인증번호 저장
    int updateVerificationCode(@Param("email") String email, @Param("code") String code);

    // ✅ 비밀번호 찾기 - 인증번호 조회
    String getVerificationCode(@Param("email") String email);
}

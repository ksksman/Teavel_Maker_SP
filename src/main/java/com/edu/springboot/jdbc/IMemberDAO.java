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
}


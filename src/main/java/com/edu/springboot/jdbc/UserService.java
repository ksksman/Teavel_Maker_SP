package com.edu.springboot.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IMemberService {

    @Autowired
    private IMemberDAO memberDAO;

    @Override
    public boolean login(MemberDTO memberDTO) {
        if (memberDTO == null || memberDTO.getEmail() == null) {
            return false; // 입력값이 없으면 로그인 실패
        }

        MemberDTO dbMember = memberDAO.login(memberDTO.getEmail());

        if (dbMember == null) {
            return false; // 사용자가 DB에 없음
        }

        return dbMember.getPassword().equals(memberDTO.getPassword()); // 비밀번호 비교 후 로그인 성공 여부 반환
    }

    @Override
    public List<MemberDTO> select() {
        return memberDAO.selectAll(); // ✅ DAO에서 전체 회원 목록 조회
    }

    @Override
    public int insert(MemberDTO memberDTO) {
        return memberDAO.insert(memberDTO); // ✅ DAO를 통해 회원 추가
    }

    @Override
    public MemberDTO selectOne(MemberDTO memberDTO) {
        return memberDAO.selectByEmail(memberDTO.getEmail()); // ✅ 이메일로 특정 회원 조회
    }

    @Override
    public int update(MemberDTO memberDTO) {
        return memberDAO.update(memberDTO); // ✅ DAO를 통해 회원 정보 업데이트
    }

    @Override
    public int delete(MemberDTO memberDTO) {
        return memberDAO.delete(memberDTO.getEmail()); // ✅ DAO를 통해 회원 삭제
    }
}

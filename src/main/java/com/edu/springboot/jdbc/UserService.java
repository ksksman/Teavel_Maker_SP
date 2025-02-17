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
        return memberDAO.selectAll(); // DAO에서 전체 회원 목록 조회
    }

    @Override
    public int insert(MemberDTO memberDTO) {
        System.out.println("🔥 회원가입 요청: " + memberDTO); // 회원가입 요청 로그 추가

        // 필수 입력값 검증
        if (memberDTO.getEmail() == null || memberDTO.getPassword() == null || 
            memberDTO.getNickname() == null || memberDTO.getBirthdate() == null) {
            System.out.println("❌ 회원가입 실패: 필수 입력값 누락");
            return -3; // 필수 입력값 누락
        }

        // 이메일 중복 체크
        if (memberDAO.selectByEmail(memberDTO.getEmail()) != null) {
            System.out.println("❌ 회원가입 실패: 이메일 중복 (" + memberDTO.getEmail() + ")");
            return -1; // 이미 가입된 이메일
        }

        // 닉네임 중복 체크
        if (memberDAO.selectByNickname(memberDTO.getNickname()) != null) {
            System.out.println("❌ 회원가입 실패: 닉네임 중복 (" + memberDTO.getNickname() + ")");
            return -2; // 이미 존재하는 닉네임
        }

        // 🚀 `marketingConsent` 기본값 설정
        if (memberDTO.getMarketingConsent() == null) {
            memberDTO.setMarketingConsent("0");
        }

        // 🚀 `recommendedFriend` 기본값 설정
        if (memberDTO.getRecommendedFriend() == null) {
            memberDTO.setRecommendedFriend("");
        }

        // 실제 DB 삽입 실행
        int result = memberDAO.insert(memberDTO);
        System.out.println("✅ 회원가입 결과 (DB 삽입 성공 여부): " + result);

        return result;
    }

    @Override
    public MemberDTO selectOne(MemberDTO memberDTO) {
        return memberDAO.selectByEmail(memberDTO.getEmail()); // 이메일로 특정 회원 조회
    }

    @Override
    public int update(MemberDTO memberDTO) {
        return memberDAO.update(memberDTO); // DAO를 통해 회원 정보 업데이트
    }

    @Override
    public int delete(MemberDTO memberDTO) {
        return memberDAO.delete(memberDTO.getEmail()); // DAO를 통해 회원 삭제
    }

    // 닉네임으로 회원 조회
    @Override
    public MemberDTO selectByNickname(String nickname) {
        return memberDAO.selectByNickname(nickname);
    }
}

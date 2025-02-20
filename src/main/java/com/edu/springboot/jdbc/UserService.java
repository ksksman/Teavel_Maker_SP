package com.edu.springboot.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService implements IMemberService {

    @Autowired
    private IMemberDAO memberDAO;

    @Autowired
    private MailService mailService; // 이메일 전송 서비스 추가

    // ✅ 인증번호 저장을 위한 캐시 (서버에서만 유지)
    private final ConcurrentHashMap<String, String> verificationCache = new ConcurrentHashMap<>();

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

    // ✅ 비밀번호 찾기 - 이메일로 인증번호 전송 (DB 저장 X)
    public boolean sendVerificationCode(String email) {
        MemberDTO member = memberDAO.selectByEmail(email);
        if (member == null) {
            return false; // 가입된 이메일이 아닐 경우
        }

        // 6자리 랜덤 인증번호 생성
        String code = String.format("%06d", new Random().nextInt(1000000));

        // ✅ 인증번호를 캐시에 저장 (이메일 -> 인증번호)
        verificationCache.put(email, code);

        // ✅ 이메일 전송
        mailService.sendEmail(email, "비밀번호 찾기 인증번호", "인증번호: " + code);

        return true;
    }

    // ✅ 비밀번호 찾기 - 인증번호 확인 (DB 조회 X)
    public boolean verifyCode(String email, String code) {
        // ✅ 캐시에서 인증번호 조회 및 검증
        String storedCode = verificationCache.get(email);
        return storedCode != null && storedCode.equals(code);
    }

    // ✅ 비밀번호 변경 메서드 추가
    @Transactional
    public int updatePassword(String email, String newPassword) {
        System.out.println("🔹 updatePassword 실행됨: email = " + email + ", newPassword = " + newPassword);
        
        MemberDTO member = memberDAO.selectByEmail(email);
        if (member == null) {
            System.out.println("❌ 이메일이 DB에 없음: " + email);
            return 0; // 이메일이 DB에 없으면 업데이트하지 않음
        }

        // 🚀 비밀번호 해싱 (선택 사항: 보안을 위해 추가 가능)
        String hashedPassword = newPassword; // 나중에 BCrypt 적용 가능

        int result = memberDAO.updatePassword(email, hashedPassword);
        System.out.println("🔹 updatePassword 실행 결과: " + result);
        return result;
    }
}

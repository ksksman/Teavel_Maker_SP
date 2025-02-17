package com.edu.springboot;

import com.edu.springboot.jdbc.IMemberDAO;
import com.edu.springboot.jdbc.MemberDTO;
import com.edu.springboot.jdbc.UserService;
import com.edu.springboot.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private IMemberDAO memberDAO;

    // 서버 상태 확인용 테스트 API
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Spring Boot API is running!");
    }

 // ✅ 로그인 API (JWT 발급 및 쿠키 설정)
    @PostMapping("/login")
    public String login(@RequestBody MemberDTO memberDTO, HttpServletResponse response) {
        MemberDTO dbMember = memberDAO.login(memberDTO.getEmail());

        if (dbMember == null || !dbMember.getPassword().equals(memberDTO.getPassword())) {
            return "아이디 또는 비밀번호가 틀렸습니다.";
        }

        // ✅ JWT 토큰 생성
        String token = JwtUtil.generateToken(memberDTO.getEmail());

        // ✅ HttpOnly 쿠키에 JWT 저장 (JavaScript에서 접근 불가, 보안 강화)
        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // HTTPS 환경에서만 작동 (개발 환경에서는 false로 변경 가능)
        cookie.setPath("/");
        cookie.setMaxAge(86400); // 1일 (초 단위)
        response.addCookie(cookie);

        return "로그인 성공";
    }

    // ✅ 로그인 상태 확인 API (쿠키에서 JWT 검증)
    @GetMapping("/me")
    public MemberDTO getUser(@CookieValue(value = "authToken", required = false) String token) {
        if (token == null) {
            return null; // 로그인되지 않음
        }

        String email = JwtUtil.validateToken(token);
        if (email == null) {
            return null; // 유효하지 않은 토큰
        }

        return memberDAO.selectByEmail(email); // 이메일로 회원 정보 조회
    }

    // ✅ 로그아웃 (쿠키 삭제)
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 쿠키 삭제
        response.addCookie(cookie);

        return "로그아웃 성공";
    }

    // 이메일 중복 확인 API (회원가입 전에 호출)
    @GetMapping("/check-email/{email}")
    public ResponseEntity<String> checkEmailDuplicate(@PathVariable String email) {
        MemberDTO existingMember = userService.selectOne(new MemberDTO(email));

        if (existingMember != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 이메일입니다.");
        }
    }

    // 회원 가입 API (POST 요청)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberDTO member) {
        // 입력값 검증
        if (member.getEmail() == null || member.getPassword() == null || 
            member.getNickname() == null || member.getBirthdate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("필수 입력값이 누락되었습니다.");
        }

        // 이메일 중복 체크
        if (userService.selectOne(new MemberDTO(member.getEmail())) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다.");
        }

        // 회원가입 진행
        int result = userService.insert(member);
        if (result > 0) {
            return ResponseEntity.ok("회원가입 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        }
    }

    // 특정 이메일로 회원 정보 조회 (GET 요청)
    @GetMapping("/profile/{email}")
    public ResponseEntity<MemberDTO> getUserProfile(@PathVariable String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        MemberDTO member = userService.selectOne(new MemberDTO(email));
        return (member != null) ? ResponseEntity.ok(member)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // 닉네임 중복 확인 API (회원가입 전에 호출)
    @GetMapping("/check-nickname/{nickname}")
    public ResponseEntity<String> checkNicknameDuplicate(@PathVariable("nickname") String nickname) {
        MemberDTO existingMember = userService.selectByNickname(nickname); // 닉네임 조회

        if (existingMember != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 닉네임입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 닉네임입니다.");
        }
    }

}

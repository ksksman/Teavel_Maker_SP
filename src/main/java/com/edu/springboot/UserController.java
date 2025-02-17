package com.edu.springboot;

import com.edu.springboot.jdbc.MemberDTO;
import com.edu.springboot.jdbc.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.edu.springboot.jdbc.MailService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 서버 상태 확인용 테스트 API
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Spring Boot API is running!");
    }

    // 로그인 API (POST 요청)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody MemberDTO member) {
        if (member == null || member.getEmail() == null || member.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이메일과 비밀번호를 입력해주세요.");
        }

        boolean success = userService.login(member);

        if (success) {
            return ResponseEntity.ok("로그인 성공");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("아이디 또는 비밀번호가 틀렸습니다.");
        }
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
 // ✅ 1. 이메일로 인증번호 전송 (DB 저장 X)
    @PostMapping("/find-password")
    public ResponseEntity<String> sendVerificationCode(@RequestParam("email") String email) {
        boolean success = userService.sendVerificationCode(email);
        if (success) {
            return ResponseEntity.ok("이메일로 인증번호가 전송되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입되지 않은 이메일입니다.");
        }
    }

    // ✅ 2. 인증번호 검증 (DB 조회 X)
    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam("email") String email, @RequestParam("code") String code) {
        boolean isValid = userService.verifyCode(email, code);
        if (isValid) {
            return ResponseEntity.ok("인증 성공!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 일치하지 않습니다.");
        }
    }
 // ✅ 3. 비밀번호 재설정 API 추가
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword) {
        int result = userService.updatePassword(email, newPassword);
        
        if (result > 0) {
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호 변경 실패.");
        }
    }
}


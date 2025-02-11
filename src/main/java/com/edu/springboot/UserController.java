package com.edu.springboot;

import com.edu.springboot.jdbc.MemberDTO;
import com.edu.springboot.jdbc.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000") // ✅ React 요청 허용
@RestController
@RequestMapping("/api/user") // ✅ API 기본 경로
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ 서버 상태 확인용 테스트 API
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Spring Boot API is running!");
    }

    // ✅ 로그인 API (POST 요청)
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

    // ✅ 회원 가입 API (POST 요청)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberDTO member) {
        int result = userService.insert(member);
        if (result > 0) {
            return ResponseEntity.ok("회원가입 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입 실패");
        }
    }

    // ✅ 특정 이메일로 회원 정보 조회 (GET 요청)
    @GetMapping("/profile/{email}") // 🔥 @PathVariable 추가해야 정상 동작!
    public ResponseEntity<MemberDTO> getUserProfile(@PathVariable String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        MemberDTO member = userService.selectOne(new MemberDTO(email)); // ✅ 기존 코드 수정
        return (member != null) ? ResponseEntity.ok(member) // 회원 정보 반환
                : ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 오류 반환
    }
}
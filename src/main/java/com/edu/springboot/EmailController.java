package com.edu.springboot;

import com.edu.springboot.jdbc.MailService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final MailService mailService;
    private final Map<String, String> verificationCodes = new HashMap<>();

    public EmailController(MailService mailService) {
        this.mailService = mailService;
    }

    // ✅ 이메일 인증번호 전송 API
    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestParam("email") String email) {
        String code = generateVerificationCode();
        verificationCodes.put(email, code);
        mailService.sendEmail(email, "이메일 인증 코드", "<h3>인증 코드: " + code + "</h3>");
        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    // ✅ 인증번호 확인 API
    @PostMapping("/verify")
    public ResponseEntity<Map<String, String>> verifyCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        Map<String, String> response = new HashMap<>();
        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            response.put("status", "success");
            response.put("message", "인증 성공!");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "fail");
            response.put("message", "인증 실패!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }


    // ✅ 6자리 인증번호 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
    
}
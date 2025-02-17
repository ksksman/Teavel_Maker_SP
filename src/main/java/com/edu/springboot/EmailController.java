package com.edu.springboot;

import com.edu.springboot.jdbc.MailService;
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
    public String sendVerificationCode(@RequestParam("email") String email) {
        String code = generateVerificationCode();
        verificationCodes.put(email, code);
        mailService.sendEmail(email, "이메일 인증 코드", "<h3>인증 코드: " + code + "</h3>");
        return "인증번호가 전송되었습니다.";
    }

    // ✅ 인증번호 확인 API
    @PostMapping("/verify")
    public String verifyCode(
            @RequestParam("email") String email,
            @RequestParam("code") String code
    ) {
        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            return "인증 성공!";
        } else {
            return "인증 실패!";
        }
    }

    // ✅ 6자리 인증번호 생성
    private String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}

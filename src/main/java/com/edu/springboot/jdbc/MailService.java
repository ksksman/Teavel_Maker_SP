package com.edu.springboot.jdbc;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}") // ✅ 발신자 이메일 환경 변수로 설정
    private String fromEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            if (fromEmail == null || fromEmail.isEmpty()) {
                throw new MessagingException("❌ 발신자 이메일이 설정되지 않았습니다.");
            }

            // ✅ 이메일 공백 제거 및 유효성 검사
            to = to.trim();
            fromEmail = fromEmail.trim();
            validateEmailFormat(to);
            validateEmailFormat(fromEmail);

            logger.info("📩 [이메일 전송 시도] from: {}, to: {}", fromEmail, to);

            // ✅ MIME 형식 이메일 생성
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // HTML 허용

            // ✅ 이메일 전송
            mailSender.send(message);
            logger.info("✅ 이메일 전송 성공: {}", to);

        } catch (MessagingException e) {
            logger.error("❌ 이메일 전송 실패: {}", e.getMessage(), e);
        }
    }

    // ✅ 이메일 형식 검사 (InternetAddress 사용)
    private void validateEmailFormat(String email) throws MessagingException {
        try {
            if (email.contains(" ")) {
                throw new MessagingException("❌ 이메일 주소에 공백이 포함됨: " + email);
            }
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (Exception e) {
            throw new MessagingException("❌ 잘못된 이메일 주소 형식: " + email, e);
        }
    }
}

package com.edu.springboot.ExceptionHandler; // 대소문자 정확히 맞춰야 함!

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage()); // HTTP 400 상태코드 + 메시지 반환
    }
}

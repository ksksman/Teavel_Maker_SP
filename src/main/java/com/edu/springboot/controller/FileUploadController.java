package com.edu.springboot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FileUploadController {

    // 파일 저장 경로 (수동으로 생성하거나, 권한 문제가 없도록 설정)
    private static final String UPLOAD_DIR = "C:/uploads/";

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일이 없습니다."));
        }

        try {
            // 저장할 폴더가 없으면 생성
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "업로드 디렉토리 생성 실패"));
            }

            // 고유 파일명 생성 (중복 방지)
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.trim().isEmpty()) {
                originalFilename = "unknownfile";
            }
            String fileName = System.currentTimeMillis() + "_" + originalFilename;
            String filePath = UPLOAD_DIR + fileName;

            // 파일 저장
            file.transferTo(new File(filePath));

            // 정적 리소스로 접근 가능한 URL 반환 (백엔드 정적 리소스 매핑 설정 필요)
            String fileUrl = "/uploads/" + fileName;

            return ResponseEntity.ok(Map.of("imageUrl", fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "파일 업로드 실패: " + e.getMessage()));
        }
    }
}

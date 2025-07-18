package com.join.spring_resume._core.common;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    // 사진 경로
    private static final String RESUME_UPLOAD_DIR = "C:/join-uploads/resume-images/";
    // private static final String MEMBER_UPLOAD_DIR = "C:/join-uploads/member-images/";
    // private static final String BOARD_UPLOAD_DIR = "C:/join-uploads/board-images/";

    public String saveResumePhoto(MultipartFile file) {
        return saveFile(file, RESUME_UPLOAD_DIR);
    }

    public void deleteResumePhoto(String fileName) {
        deleteFile(fileName, RESUME_UPLOAD_DIR);
    }

    // 수정
    private String saveFile(MultipartFile file, String uploadDir) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + newFileName);
        try {
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());
            return newFileName;
        } catch (IOException e) {
            System.err.println("파일 저장 실패: " + newFileName + ", 에러: " + e.getMessage());
            throw new RuntimeException("파일 저장에 실패했습니다.", e);
        }
    }

    // 삭제
    private void deleteFile(String fileName, String uploadDir) {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }
        try {
            Path filePath = Paths.get(uploadDir + fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("파일 삭제 실패: " + fileName + ", 에러: " + e.getMessage());
        }
    }
}
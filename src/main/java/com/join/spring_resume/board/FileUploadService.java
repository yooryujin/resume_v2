package com.join.spring_resume.board;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadService {

    // 상대 경로 기준: 현재 프로젝트 루트/upload/
    private static final String UPLOAD_DIR = new File("upload/").getAbsolutePath() + "/";

    public String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("빈 파일입니다.");
        }

        // 파일명 생성
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID() + extension;

        // 저장 파일 객체 생성
        File targetFile = new File(UPLOAD_DIR + uniqueFileName);

        // 디렉토리 없으면 생성
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        // 파일 저장
        file.transferTo(targetFile);

        // 정적 자원으로 접근할 수 있도록 URL 반환
        return "/upload/" + uniqueFileName;
    }
}

package com.join.spring_resume.resume;

import com.join.spring_resume.career.CareerRequest;
import com.join.spring_resume.member.Member;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ResumeRequest {

    //이력서 저장 DTO
    @Data
    public static class SaveDTO {
        @NotBlank(message = "제목은 필수 입력 값입니다.")
        @Size(max = 50, message = "제목은 50자 이내로 작성해주세요.")
        private String resumeTitle;
        @NotBlank(message = "내용은 필수 입력 값입니다.")
        @Size(max = 5000, message = "내용은 5000자 이내로 작성해주세요.")
        private String resumeContent;
        private Boolean isRep;

        private MultipartFile photo; // 사진 파일을 받기 위한 필드

        @Valid
        private List<CareerRequest.SaveDTO> careers = new ArrayList<>();

        public Resume toEntity(Member member){
            return Resume.builder()
                    .resumeTitle(this.resumeTitle)
                    .resumeContent(this.resumeContent)
                    .isRep(Boolean.TRUE.equals(this.isRep)) // null일 경우 false가 되도록 처리
                    .member(member)
                    .build();
        }

    }//saveDTO


    //이력서 수정 DTO
    @Data
    public static class UpdateDTO {
        @NotBlank(message = "제목은 필수 입력 값입니다.")
        @Size(max = 50, message = "제목은 50자 이내로 작성해주세요.")
        private String resumeTitle;
        @NotBlank(message = "내용은 필수 입력 값입니다.")
        @Size(max = 5000, message = "내용은 5000자 이내로 작성해주세요.")
        private String resumeContent;
        private Boolean isRep;

        private MultipartFile photo; // 👈 사진 수정을 위한 필드 추가

        @Valid
        private List<CareerRequest.UpdateDTO> careers = new ArrayList<>();

        private List<Long> deletedCareerIds = new ArrayList<>();

    }//UpdateDTO

}//

package com.join.spring_resume.resume;

import com.join.spring_resume._core.common.PageResponseDTO;
import com.join.spring_resume.career.Career;
import com.join.spring_resume.career.CareerResponse;
import com.join.spring_resume.member.Member;
import com.join.spring_resume.member.MemberResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResumeResponse {

    /**
     * 이력서 수정 페이지를 위한 DTO
     * - update-form.mustache가 필요로 하는 데이터만 담았습니다.
     * - 엔티티의 모든 필드가 아닌, 화면에 필요한 필드만 노출하여 보안을 강화합니다.
     */
    @Data
    public static class UpdateFormDTO {
        private Long resumeIdx;
        private String resumeTitle;
        private String resumeContent;
        private Boolean isRep;
        private List<CareerResponse.CareerDTO> careerList;

        // Entity를 DTO로 변환하는 생성자
        public UpdateFormDTO(Resume resume) {
            this.resumeIdx = resume.getResumeIdx();
            this.resumeTitle = resume.getResumeTitle();
            this.resumeContent = resume.getResumeContent();
            this.isRep = resume.getIsRep();
            this.careerList = new ArrayList<>(); //조심, 많이 실수하는 부분
            for (Career career : resume.getCareerList()) {
                this.careerList.add(new CareerResponse.CareerDTO(career));
            }
        }
    }

    // 기업 채용담당관을 위한 이력서 상세보기
    @Data
    public static class CorpDetailDTO {
        private Long resumeIdx;
        private String resumeTitle;
        private String resumeContent;
        private MemberResponse.MemberDTO member;
        private List<CareerResponse.CareerDTO> careerList;
        private Boolean isOwner;

        // Resume 엔티티를 받아서 이 DTO를 채우는 생성자
        public CorpDetailDTO(Resume resume) {
            this.resumeIdx = resume.getResumeIdx();
            this.resumeTitle = resume.getResumeTitle();
            this.resumeContent = resume.getResumeContent();
            this.member = MemberResponse.MemberDTO.fromEntity(resume.getMember());
            this.isOwner = false;

            this.careerList = new ArrayList<>(); //조심, 많이 실수하는 부분
            for (Career career : resume.getCareerList()) {
                this.careerList.add(new CareerResponse.CareerDTO(career));
            }
        }
    }

    /**
     * 이력서 목록보기 페이징을 위한 DTO
     */
    @Data
    @AllArgsConstructor
    public static class ListDTO {
        private ResumeDTO repResume; //대표이력서
        private PageResponseDTO<ResumeDTO> resumePage; // 일반이력서

        //총 이력서 개수 카운트
        public long getTotalCount() {
            long count = resumePage.getTotalElements();
            if (repResume != null) {
                count++;
            }
            return count;
        }

    }

    /**
     * 이력서 목록보기의 일반이력서를 나타내는 DTO
     */
    @Data
    public static class ResumeDTO {
        private Long resumeIdx;
        private String resumeTitle;
        private String shortContent;
        private String createdAtFormatted;

        public ResumeDTO(Resume resume) {
            this.resumeIdx = resume.getResumeIdx();
            this.resumeTitle = resume.getResumeTitle();
            this.shortContent = resume.getShortContent();
            this.createdAtFormatted = resume.getCreatedAtFormatted();
        }
    }

    /**
     * 개인회원 이력서 상세보기 페이지를 위한 DTO
     * - detail.mustache 뷰가 필요로 하는 모든 데이터를 담고 있다.
     */
    @Data
    public static class DetailDTO {
        private Long resumeIdx;
        private String resumeTitle;
        private String resumeContent;
        private MemberResponse.MemberDTO member;
        private List<CareerResponse.CareerDTO> careerList;
        private Boolean isOwner;

        // Resume 엔티티를 이 DTO로 변환하는 생성자
        public DetailDTO(Resume resume) {
            this.resumeIdx = resume.getResumeIdx();
            this.resumeTitle = resume.getResumeTitle();
            this.resumeContent = resume.getResumeContent();
            this.member = MemberResponse.MemberDTO.fromEntity(resume.getMember());

            this.careerList = new ArrayList<>(); //조심, 많이 실수하는 부분
            for (Career career : resume.getCareerList()) {
                this.careerList.add(new CareerResponse.CareerDTO(career));
            }

        }


    }

}
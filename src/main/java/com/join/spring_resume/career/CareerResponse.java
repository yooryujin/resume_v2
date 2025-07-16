package com.join.spring_resume.career;

import lombok.Data;

import java.time.format.DateTimeFormatter;

public class CareerResponse {

    /**
     * 경력 정보를 화면에 보여주기 위한 공통 DTO
     * - 이력서 상세보기, 수정 등 다양한 곳에서 재사용됩니다.
     */
    @Data
    public static class CareerDTO {
        private Long careerIdx;
        private String corpName;
        private String position;
        private String careerContent;
        private String startAt;
        private String endAt;

        public CareerDTO(Career career) {
            this.careerIdx = career.getCareerIdx();
            this.corpName = career.getCorpName();
            this.position = career.getPosition();
            this.careerContent = career.getCareerContent();

            // 📅 LocalDate를 'yyyy-MM-dd' 형식의 문자열로 변환
            if (career.getStartAt() != null) {
                this.startAt = career.getStartAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
            if (career.getEndAt() != null) {
                this.endAt = career.getEndAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        }
    }
}
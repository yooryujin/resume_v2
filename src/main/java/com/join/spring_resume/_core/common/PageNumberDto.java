package com.join.spring_resume._core.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 페이지네이션 UI(번호)를 생성하고 관리하기 위한 DTO
 */

@Data
public class PageNumberDto {

    private final int number;
    private final int displayNumber;
    private final boolean current;

    //개별 페이지 번호 객체를 생성
    public PageNumberDto(int number, int currentPage) {
        this.number = number;
        this.displayNumber = number + 1;
        this.current = (number == currentPage);
    }

    //뷰에 전달될 페이지네이션 정보를 담는 DTO
    @Data
    public static class PageNavigation {
        private final List<PageNumberDto> pageNumbers;
        private final boolean hasPrev;
        private final boolean hasNext;
        private final int prevPage;
        private final int nextPage;
    }

    //Page 객체를 기반으로 페이지네이션 UI를 생성
    public static PageNavigation createNavigation(Page<?> page) {
        int totalPages = page.getTotalPages();
        int currentPage = page.getNumber();
        int displayRange = 5;

        // 1. 표시할 시작 페이지 번호 계산
        int startPage = Math.max(0, currentPage - displayRange / 2);

        // 2. 표시할 끝 페이지 번호 계산
        int endPage = Math.min(startPage + displayRange - 1, totalPages - 1);

        // 3. 끝 페이지 번호 보정
        if (endPage - startPage < displayRange - 1) {
            startPage = Math.max(0, endPage - displayRange + 1);
        }

        // 4. 페이지 번호 목록 생성
        List<PageNumberDto> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(new PageNumberDto(i, currentPage));
        }

        // 5. 이전, 다음 버튼 상태 계산
        boolean hasPrev = currentPage > 0;
        boolean hasNext = currentPage < totalPages - 1;
        int prevPage = hasPrev ? currentPage - 1 : 0;
        int nextPage = hasNext ? currentPage + 1 : totalPages - 1;

        return new PageNavigation(pageNumbers, hasPrev, hasNext, prevPage, nextPage);
    }

    //PageResponseDTO를 받아서 네비게이션을 생성
    //원본 메서드와 입력받는 객체의 타입만 다름
    public static PageNavigation createNavigation(PageResponseDTO<?> pageDto) {
        int totalPages = pageDto.getTotalPages();
        int currentPage = pageDto.getPage(); // getNumber() 대신 getPage() 사용
        int displayRange = 5;

        int startPage = Math.max(0, currentPage - displayRange / 2);
        int endPage = Math.min(startPage + displayRange - 1, totalPages - 1);

        if (endPage - startPage < displayRange - 1) {
            startPage = Math.max(0, endPage - displayRange + 1);
        }

        List<PageNumberDto> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(new PageNumberDto(i, currentPage));
        }

        boolean hasPrev = currentPage > 0;
        boolean hasNext = currentPage < totalPages - 1;
        int prevPage = hasPrev ? currentPage - 1 : 0;
        int nextPage = hasNext ? currentPage + 1 : totalPages - 1;

        return new PageNavigation(pageNumbers, hasPrev, hasNext, prevPage, nextPage);
    }
}

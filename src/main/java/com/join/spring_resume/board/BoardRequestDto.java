package com.join.spring_resume.board;


import com.join.spring_resume.member.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDto {

    private String boardTitle;
    private String boardContent;
    private String tags;

    // 생성 시 엔티티 변환
    public Board toEntity(Member member) {
        Board board = new Board();
        board.setBoardTitle(this.boardTitle);
        board.setBoardContent(this.boardContent);
        board.setTags(this.tags);
        board.setBoardHits(0);
        board.setMember(member);
        return board;
    }

    // 수정 시 엔티티에 값 적용
    public void  applyTo(Board board) {
        board.setBoardTitle(this.boardTitle);
        board.setBoardContent(this.boardContent);
        board.setTags(this.tags);
    }


}

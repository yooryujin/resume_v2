package com.join.spring_resume.resume;

import com.join.spring_resume._core.common.FileService;
import com.join.spring_resume._core.common.PageResponseDTO;
import com.join.spring_resume._core.errors.exception.Exception403;
import com.join.spring_resume._core.errors.exception.Exception404;
import com.join.spring_resume.career.Career;
import com.join.spring_resume.career.CareerJpaRepository;
import com.join.spring_resume.career.CareerRequest;
import com.join.spring_resume.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ResumeService {

    private final ResumeJpaRepository resumeJpaRepository;
    private final CareerJpaRepository careerJpaRepository;
    private final FileService fileService;
    //private static final String UPLOAD_DIR = "C:/join-uploads/resume-images/";

    // 관리자용 이력서 전체조회
    public List<Resume> findAll() {
        return resumeJpaRepository.findAll();
    }

    // 회원 이력서 전체조회
    public List<Resume> findMyResumes(Long memberIdx) {
        return resumeJpaRepository.findAllByMemberIdx(memberIdx);
    }

    // 회원 이력서 카운트
    public int countMyResume(Long memberIdx) {
        return resumeJpaRepository.countByMemberIdx(memberIdx);
    }

    // 이력서와 경력 동시 조회
    public Resume findByIdWithCareers(Long resumeIdx) {
        // 이전에 추가했던 JOIN FETCH 쿼리를 사용합니다.
        return resumeJpaRepository.findByIdWithCareers(resumeIdx)
                .orElseThrow(() -> new Exception404("해당 이력서를 찾을 수 없습니다. id: " + resumeIdx));
    }

    //개인회원 이력서 상세보기
    public ResumeResponse.DetailDTO findMyResumeDetail(Long resumeIdx, Long sessionUserId) {
        // 이력서를 경력과 함께 조회 (기존 메소드 재활용)
        Resume resume = findByIdWithCareers(resumeIdx);
        // 소유권 확인 (서비스의 책임)
        if (!resume.isOwner(sessionUserId)) {
            throw new Exception403("이력서를 조회할 권한이 없습니다");
        }
        // DTO로 변환 후 반환 (서비스의 책임)
        return new ResumeResponse.DetailDTO(resume);
    }

    // 기업 채용담당관용 이력서 상세보기
    public ResumeResponse.CorpDetailDTO findCorpResumeDetail(Long resumeIdx) {
        Resume resume = resumeJpaRepository.findByIdWithCareers(resumeIdx)
                .orElseThrow(() -> new Exception404("해당 이력서를 찾을 수 없습니다: " + resumeIdx));
        // DTO로 변환해서 반환
        return new ResumeResponse.CorpDetailDTO(resume);
    }

    // 페이징된 이력서 목록 조회
    public ResumeResponse.ListDTO findResumesForList(Long memberIdx, Pageable pageable) {
        // 대표 이력서 엔티티 조회
        Resume repResume = resumeJpaRepository.findRepresentativeResumeByMemberIdx(memberIdx)
                .orElse(null);
        // 대표이력서 DTO 변환
        ResumeResponse.ResumeDTO repResumeDto = (repResume != null)
                ? new ResumeResponse.ResumeDTO(repResume) : null;
        // 일반이력서 페이징해 조회
        Page<Resume> resumePage = resumeJpaRepository.findByMemberIdxAndIsRepFalse(memberIdx, pageable);
        // 조회된 일반이력서 DTO 변환
        PageResponseDTO<ResumeResponse.ResumeDTO> resumeDtoPage = PageResponseDTO.from(
                resumePage,
                ResumeResponse.ResumeDTO::new
        );
        // ListDTO에 담아 반환
        return new ResumeResponse.ListDTO(repResumeDto, resumeDtoPage);
    }

    // 이력서 저장
    @Transactional
    public Resume save(ResumeRequest.SaveDTO saveDTO, Member sessionMember) {
        // 파일처리, 파일명 저장
        String photoFileName = fileService.saveResumePhoto(saveDTO.getPhoto());
        // 이력서 엔티티 생성
        Resume resume = saveDTO.toEntity(sessionMember);
        // 업로드된 사진 파일명 설정
        resume.setResumePhoto(photoFileName);
        // 대표 이력서 설정
        updateRepStatus(resume, saveDTO.getIsRep(), sessionMember.getMemberIdx());
        // 이력서 저장
        Resume savedResume = resumeJpaRepository.save(resume);
        // 경력 저장
        saveCareers(saveDTO.getCareers(), savedResume);
        // 이력서 반환
        return savedResume;
    }

    // 이력서 수정 페이지 렌더링
    public ResumeResponse.UpdateFormDTO findUpdateForm(Long resumeIdx, Long sessionUserId) {

        // Repository를 통해 Entity 조회
        Resume resume = resumeJpaRepository.findByIdWithCareers(resumeIdx)
                .orElseThrow(() -> new Exception404("해당 이력서를 찾을 수 없습니다. id: " + resumeIdx));
        // 이력서 소유권을 확인
        if (!resume.isOwner(sessionUserId)) {
            throw new Exception403("이력서를 수정할 권한이 없습니다.");
        }
        // Entity를 DTO로 변환하여 반환
        return new ResumeResponse.UpdateFormDTO(resume);
    }

    //이력서 수정
    @Transactional
    public void updateById(Long resumeIdx, ResumeRequest.UpdateDTO updateDTO, Member sessionMember) {
        // 이력서 조회 및 소유권 확인
        Resume resume = resumeJpaRepository.findById(resumeIdx)
                .orElseThrow(() -> new Exception404("해당 이력서를 찾을 수 없습니다. id=" + resumeIdx));
        if (!resume.isOwner(sessionMember.getMemberIdx())) {
            throw new Exception403("이력서를 수정할 권한이 없습니다.");
        }
        // 이력서 기본 정보 수정 (JPA의 더티 체킹으로 자동 UPDATE)
        resume.setResumeTitle(updateDTO.getResumeTitle());
        resume.setResumeContent(updateDTO.getResumeContent());
        // 경력, 사진, 대표이력서 수정 헬퍼 호출
        updateCareers(updateDTO, resume);
        updatePhoto(updateDTO.getPhoto(), resume);
        updateRepStatus(resume, updateDTO.getIsRep(), sessionMember.getMemberIdx());
    }

    //이력서 삭제
    @Transactional
    public void deleteById(Long resumeIdx, Member member) {
        // 이력서 조회 (연관된 Career는 JPA가 자동으로 처리하므로 findById로 충분)
        Resume resume = resumeJpaRepository.findById(resumeIdx)
                .orElseThrow(() -> new Exception404("해당 이력서를 찾을 수 없습니다. id: " + resumeIdx));

        // 소유권 확인
        if (!resume.isOwner(member.getMemberIdx())) {
            throw new Exception403("본인이 작성한 이력서만 삭제할 수 있습니다");
        }

        // 사진 삭제
        fileService.deleteResumePhoto(resume.getResumePhoto());

        // DB에서 이력서 삭제 (연관된 Career도 CascadeType.REMOVE로 함께 삭제됨)
        resumeJpaRepository.delete(resume);
    }

    // 대표이력서 찾기
    public Resume findRepResumeByMember(Member member) {
        return resumeJpaRepository.findRepresentativeResumeByMember(member)
                .orElseThrow(() -> new Exception404("대표 이력서가 존재하지 않습니다."));
    }

    // 대표 이력서 간편 수정
    @Transactional
    public void setRep(Long memberIdx, Long resumeIdx) {

        Resume resume = resumeJpaRepository.findById(resumeIdx).orElseThrow(() -> {
            return new Exception404("이력서를 찾을 수 없습니다" + resumeIdx);
        });
        if (!resume.isOwner(memberIdx)) {
            throw new Exception403("대표이력서를 수정할 권한이 없습니다.");
        }
        //헬퍼메서드 위임
        updateRepStatus(resume, true, memberIdx);
    }

    /// \\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\
    /// 여기서부터는 헬퍼 메서드
    /// 1.경력사항 저장
    /// 2.경력사항 수정
    /// 3.사진 저장
    /// 4.사진 수정
    /// 5.대표이력서 수정
    /// \\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\///\\\

    // 경력사항 저장 (save)
    private void saveCareers(List<CareerRequest.SaveDTO> careerDTOs, Resume savedResume) {
        if (careerDTOs == null || careerDTOs.isEmpty()) {
            return;
        }
        List<Career> careers = careerDTOs.stream()
                .filter(dto -> dto.getCorpName() != null && !dto.getCorpName().trim().isEmpty()) // 내용이 비어있는 폼은 무시
                .map(dto -> dto.toEntity(savedResume))
                .collect(Collectors.toList());

        if (!careers.isEmpty()) {
            careerJpaRepository.saveAll(careers);
        }
    }

    // 경력사항 수정 (update) - 안정성 강화 버전
    private void updateCareers(ResumeRequest.UpdateDTO updateDTO, Resume resume) {
        // 1. 삭제 (DELETE)
        // 클라이언트가 삭제하도록 요청한 경력 ID 목록을 가져와서 한 번에 삭제합니다.
        // 이 로직은 가장 먼저 실행되어야 다른 로직에 영향을 주지 않습니다.
        if (updateDTO.getDeletedCareerIds() != null && !updateDTO.getDeletedCareerIds().isEmpty()) {
            careerJpaRepository.deleteAllByIdInBatch(updateDTO.getDeletedCareerIds());
        }

        List<CareerRequest.UpdateDTO> careerDTOs = updateDTO.getCareers();
        if (careerDTOs == null || careerDTOs.isEmpty()) {
            return; // 처리할 경력이 없으면 여기서 종료
        }

        // 2. 생성 (CREATE)
        // DTO 목록에서 careerIdx가 없는 것들만 필터링하여 '새로운 경력'으로 간주합니다.
        // 비어있는 폼은 제외하고, 엔티티로 변환하여 한 번에 저장합니다.
        List<Career> newCareers = careerDTOs.stream()
                .filter(dto -> dto.getCareerIdx() == null) // 새로 추가된 경력만 선택
                .filter(dto -> dto.getCorpName() != null && !dto.getCorpName().trim().isEmpty()) // 내용이 비어있는 폼은 무시
                .map(dto -> dto.toEntity(resume)) // Career 엔티티로 변환
                .collect(Collectors.toList());

        if (!newCareers.isEmpty()) {
            careerJpaRepository.saveAll(newCareers); // 새로운 경력들을 DB에 일괄 저장
        }

        // 참고: 현재 프론트엔드는 기존 경력의 '수정'을 지원하지 않으므로,
        // '수정' 로직은 포함하지 않았습니다. 이 코드는 현재 화면의 동작(삭제와 추가)에 완벽히 대응합니다.
    }

    // 사진 수정 (update)
    private void updatePhoto(MultipartFile newPhoto, Resume resume) {
        if (newPhoto == null || newPhoto.isEmpty()) {
            return; // 새 사진이 없으면 아무것도 하지 않음
        }
        // FileService를 통해 새 파일을 저장하고 파일명을 받아옴
        String newPhotoFilename = fileService.saveResumePhoto(newPhoto);
        // 기존 파일명을 가져옴
        String oldPhotoFilename = resume.getResumePhoto();
        // DB에 새 파일명 업데이트
        resume.setResumePhoto(newPhotoFilename);
        // 기존 파일이 있다면 FileService를 통해 삭제
        if (oldPhotoFilename != null) {
            fileService.deleteResumePhoto(oldPhotoFilename);
        }
    }

    // 대표이력서 수정 (save, update, setRep)
    private void updateRepStatus(Resume resume, Boolean isRep, Long memberIdx) {
        if (Boolean.TRUE.equals(isRep)) {
            resumeJpaRepository.resetAllIsRepByMemberIdx(memberIdx);
            resume.setIsRep(true);
        } else {
            resume.setIsRep(false);
        }
    }

}//

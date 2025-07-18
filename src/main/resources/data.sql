INSERT INTO `corp_tb` (`corp_idx`, `corp_name`, `corp_id`, `email`, `password`, `corp_image`) VALUES
(1, '삼성전자', 'samsung1', 'samsung1@corp.com', '1234', 'basic.png'),
(2, 'LG전자', 'lg2', 'lg2@corp.com', '1234', 'basic.png'),
(3, '카카오', 'kakao3', 'kakao3@corp.com', '1234', 'basic.png'),
(4, '네이버', 'naver4', 'naver4@corp.com', '1234', 'basic.png');

-- 2. member_tb (회원 정보) - address_default, address_detail 컬럼 추가
INSERT INTO `member_tb` (`member_idx`, `username`, `password`, `member_id`, `email`, `sex`, `age`, `phone_number`, `address`, `address_default`, `address_detail`, `member_image`) VALUES
(1, '홍길동', '1234', 'hong1', 'hong1@example.com', 'M', 25, '010-1234-1111', '서울시 강남구', NULL, NULL, 'basic.png'),
(2, '김철수', '1234', 'kim2', 'kim2@example.com', 'M', 30, '010-1234-2222', '서울시 서초구', NULL, NULL, 'basic.png'),
(3, '이영희', '1234', 'lee3', 'lee3@example.com', 'F', 28, '010-1234-3333', '서울시 송파구', NULL, NULL, 'basic.png');

INSERT INTO `resume_tb` (`resume_idx`, `member_idx`, `resume_title`, `resume_content`, `is_rep`, `resume_photo`) VALUES
(1, 1, '준비된 백엔드 개발자, 홍길동입니다.', '3년간의 실무 경험을 바탕으로 안정적인 서비스를 만드는 것에 자신있습니다. Java, Spring Boot, JPA, MySQL 환경에 익숙하며, AWS를 활용한 배포 경험이 있습니다. 대규모 트래픽 처리 및 성능 개선 경험을 통해 귀사에 기여하고 싶습니다.', 1, 'sample1.jpg'),
(2, 1, '도전을 즐기는 풀스택 개발자 이력서', 'Node.js와 React를 활용한 다양한 프로젝트 경험이 있습니다. 백엔드와 프론트엔드 양쪽 모두에 대한 이해를 바탕으로, 빠르고 유연한 프로토타입 개발이 가능합니다.', 0, NULL),
(3, 2, '성실한 개발자 김철수의 이력서', '꾸준함과 꼼꼼함이 저의 가장 큰 장점입니다. 맡은 바 임무는 반드시 완수해내는 책임감을 가지고 있으며, 새로운 기술을 배우는 것을 두려워하지 않습니다.', 1, 'sample4.png'),
(4, 3, '사용자 중심의 개발자 이영희입니다.', '더 나은 사용자 경험(UX)을 제공하기 위해 항상 고민합니다. 개발뿐만 아니라 기획 단계부터 참여하여 사용자의 목소리를 서비스에 녹여내는 일에 큰 보람을 느낍니다.', 1, 'sample5.jpg');

INSERT INTO `career_tb` (`career_idx`, `resume_idx`, `corp_name`, `position`, `start_at`, `end_at`, `career_content`) VALUES
(1, 1, '네이버', '백엔드 개발자', '2022-01-01', '2023-12-31', '네이버 쇼핑 백엔드 API 개발 및 유지보수. 특히 주문 및 결제 시스템의 성능 개선 프로젝트를 리딩하여 응답 시간을 30% 단축시키는 성과를 거두었습니다.'),
(2, 1, '쿠팡', '서버 개발자', '2020-01-01', '2021-12-31', '쿠팡이츠 초기 멤버로서 주문 중계 시스템을 개발했습니다. 마이크로서비스 아키텍처(MSA) 환경에서 안정적인 서비스를 구축하는 경험을 쌓았습니다.'),
(3, 3, 'CJ올리브네트웍스', '시스템 운영', '2022-05-01', '2024-04-30', '사내 그룹웨어 시스템 운영 및 유지보수 업무를 담당하며 안정적인 서비스 운영 노하우를 익혔습니다.');

INSERT INTO `recruit_tb` (`recruit_idx`, `corp_idx`, `recruit_title`, `area`, `recruit_number`, `career`, `education`, `work_type`, `recruit_content`) VALUES
(1, 2, '[LG전자] 웹OS 플랫폼 백엔드 개발자 모집', '서울', 3, '경력', '대졸', '정규직', 'LG 스마트 TV에 탑재되는 webOS 플랫폼의 백엔드 서비스를 개발하고 운영합니다. 대용량 트래픽 처리 경험자를 우대합니다.'),
(2, 2, '[LG전자] AI 직무 신입/경력 채용', '판교', 5, '신입/경력', '석사 이상', '정규직', 'LG전자의 미래를 이끌어갈 AI 전문가를 모십니다. 머신러닝, 딥러닝, 컴퓨터 비전 등 다양한 분야의 인재를 기다립니다.'),
(3, 1, '[삼성전자] 클라우드 서비스 개발자 채용', '수원', 10, '경력', '대졸', '정규직', '삼성 클라우드 플랫폼의 핵심 서비스를 개발하고 글로벌 서비스를 운영합니다.'),
(4, 3, '[카카오] 카카오톡 서버 개발자 모집', '성남', 5, '경력', '학력무관', '정규직', '월 5천만명이 사용하는 카카오톡의 서버를 함께 만들어나갈 열정적인 동료를 찾습니다.');

INSERT INTO `board_tb` (`board_idx`, `member_idx`, `board_title`, `board_content`, `board_hits`, `tags`) VALUES
(1, 1, 'LG전자 AI 직무 면접 보신 분 계신가요?', '이번에 LG전자 AI 직무에 지원했는데, 혹시 최근에 면접 보신 분 계시면 어떤 질문이 나왔는지, 분위기는 어땠는지 공유해주실 수 있을까요? 준비를 어떻게 해야할지 막막하네요.', 152, '#LG전자#AI#면접후기'),
(2, 1, '개발자 사이드 프로젝트, 같이 하실 분!', 'Spring Boot와 React로 간단한 SNS 서비스를 만들어보려고 합니다. 저는 백엔드를 맡을 예정이고, 프론트엔드 개발자 한 분을 모십니다. 포트폴리오 용으로 좋습니다!', 210, '#사이드프로젝트#스터디#React'),
(3, 3, '신입인데, 첫 이력서 피드백 부탁드립니다.', '안녕하세요. 이제 막 부트캠프를 수료한 신입입니다. 처음으로 이력서를 써봤는데 너무 부족한 것 같아 고민입니다. 선배님들께서 보시고 따끔한 조언 부탁드립니다.', 310, '#신입#이력서#피드백');

INSERT INTO `apply_tb` (`apply_idx`, `resume_idx`, `recruit_idx`) VALUES
(1, 1, 1),
(2, 3, 1),
(3, 4, 2),
(4, 1, 3);

INSERT INTO `recruit_like_tb` (`like_idx`, `member_idx`, `recruit_idx`) VALUES
(1, 1, 2),
(2, 1, 4),
(3, 2, 1),
(4, 3, 3);

INSERT INTO `likes` (`member_id`, `board_id`, `like_yn`) VALUES
(1, 3, true),
(2, 1, true),
(3, 2, true);

INSERT INTO `comment_tb` (`id`, `member_idx`, `board_id`, `comment`, `parent_id`, `is_secret`) VALUES
(1, 2, 1, '저도 다음주에 면접보러 가는데 긴장되네요. 보통 CS 기본기랑 프로젝트 경험 위주로 많이 물어본다고 들었습니다.', NULL, false),
(2, 3, 1, '1분 자기소개랑 지원동기는 필수로 준비해가시는게 좋아요!', NULL, false),
(3, 1, 1, '두 분 모두 조언 정말 감사합니다! 큰 도움이 되었어요.', 1, false);
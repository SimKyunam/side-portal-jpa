-- 공통코드
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES (null, 'resourceCd', '자원유형', 'ResourceCd', 1, 1);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('resourceCd', 'kt', 'KT', '1', 1, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('resourceCd', 'aws', 'AWS', '2', 2, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('resourceCd', 'azure', 'AZURE', '3', 3, 2);

INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('kt', 'ktidc', 'KT IDC', '1', 1, 3);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('kt', 'ktpl', 'KT 전용회선', '2', 2, 3);

INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES (null, 'noticeType', '공지사항구분', 'NoticeType', 2, 1);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('noticeType', 'SVC', '서비스', 'SVC', 1, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('noticeType', 'UPD', '업데이트', 'UPD', 2, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('noticeType', 'WRK', '작업/장애', 'WRK', 3, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('noticeType', 'PRM', '홍보', 'PRM', 4, 2);

INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES (null, 'qnaType', 'QnA유형', 'QnaType', 3, 1);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'LGN', '로그인', 'SVC', 1, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'DSB', '대시보드', 'UPD', 2, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'RSC', '자원', 'WRK', 3, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'MON', '모니터링', 'PRM', 4, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'CST', '비용', 'PRM', 5, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'PRJ', '프로젝트', 'PRM', 6, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'ETC', '기타', 'PRM', 7, 2);

-- 메뉴
INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (null, 1, '자원', 'R', 1, 1);

INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (1, 2, '태그', 'T', 1, 2);

INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (1, 3, '클라우드서버', 'CS', 2, 2);
INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (3, 6, '서버', 'S', 1, 3);
INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (3, 7, '볼륨', 'V', 2, 3);
INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (3, 8, '네트워크', 'N', 3, 3);
INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (3, 9, '보안그룹', 'SG', 4, 3);

INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (1, 4, '클라우드DB', 'CD', 3, 2);

INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (1, 5, 'KT인프라', 'KI', 3, 2);
INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (5, 10, 'IDC서버', 'IS', 1, 3);
INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (5, 11, 'IDC네트워크', 'IN', 2, 3);
INSERT INTO menu(parent_id, menu_id, menu_name, menu_value, ord, depth) VALUES (5, 12, 'KT전용회선', 'KL', 3, 3);




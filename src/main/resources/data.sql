INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES (null, 'resourceCd', '자원유형', null, 1, 1);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('resourceCd', 'kt', 'KT', '1', 1, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('resourceCd', 'aws', 'AWS', '2', 2, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('resourceCd', 'azure', 'AZURE', '3', 3, 2);

INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('kt', 'ktidc', 'KT IDC', '1', 1, 3);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('kt', 'ktpl', 'KT 전용회선', '2', 2, 3);

INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES (null, 'noticeType', '공지사항구분', null, 2, 1);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('noticeType', 'SVC', '서비스', 'SVC', 1, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('noticeType', 'UPD', '업데이트', 'UPD', 2, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('noticeType', 'WRK', '작업/장애', 'WRK', 3, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('noticeType', 'PRM', '홍보', 'PRM', 4, 2);


INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES (null, 'qnaType', 'QnA유형', null, 3, 1);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'LGN', '로그인', 'SVC', 1, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'DSB', '대시보드', 'UPD', 2, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'RSC', '자원', 'WRK', 3, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'MON', '모니터링', 'PRM', 4, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'CST', '비용', 'PRM', 5, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'PRJ', '프로젝트', 'PRM', 6, 2);
INSERT INTO code(parent_id, code_id, code_name, code_value, ord, depth) VALUES ('qnaType', 'ETC', '기타', 'PRM', 7, 2);
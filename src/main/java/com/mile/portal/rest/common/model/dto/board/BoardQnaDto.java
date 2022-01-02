package com.mile.portal.rest.common.model.dto.board;

import com.mile.portal.rest.common.model.domain.Attach;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardQnaDto implements Serializable {
    private Long id;
    private String title;
    private String content;
    private int readCnt;
    private String qnaType;
    private String answerContent;

    //첨부파일 카운트
    private Long fileCnt;
    private List<? extends Attach> files;

    //관리자
    private Long managerId;
    private String managerName;

    //등록 사용자
    private Long clientId;
    private String clientName;
}

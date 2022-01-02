package com.mile.portal.rest.common.model.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFaqDto implements Serializable {
    private Long id;
    private String title;
    private String content;
    private int readCnt;
    private String faqType;

    //관리자
    private Long managerId;
    private String managerName;
}

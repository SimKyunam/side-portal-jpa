package com.mile.portal.rest.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long id;
    private String ntcType;
    private String title;
    private String content;
    private int readCnt;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private String hotYn;
    private String pubYn;
    
    //관리자
    private Long managerId;
    private String managerName;
}

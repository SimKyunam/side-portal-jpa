package com.mile.portal.rest.common.model.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mile.portal.rest.base.dto.BaseDto;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardFaqDto extends BaseDto {
    private Long id;
    private String title;
    private String content;
    private int readCnt;
    private String faqType;
    private String faqTypeName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updated;

    //관리자
    private Long managerId;
    private String managerName;
}

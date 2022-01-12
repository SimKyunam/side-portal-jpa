package com.mile.portal.rest.common.model.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mile.portal.rest.base.dto.BaseDto;
import com.mile.portal.rest.common.model.domain.Attach;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardNoticeDto extends BaseDto {
    private Long id;
    private String title;
    private String content;
    private int readCnt;
    private String ntcType;
    private String ntcTypeName;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private String hotYn;
    private String pubYn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime created;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime updated;

    //첨부파일 카운트
    private Long fileCnt;
    private List<? extends Attach> files;

    //관리자
    private Long managerId;
    private String managerName;
}

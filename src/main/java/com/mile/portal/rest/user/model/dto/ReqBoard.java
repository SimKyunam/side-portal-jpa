package com.mile.portal.rest.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ReqBoard {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardFaq {
        private Long id;
        private String faqType;
        private String title;
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardNotice {
        private Long id;
        private String ntcType;
        private String title;
        private String content;
        private LocalDateTime beginDate;
        private LocalDateTime endDate;
        private String hotYn;
        private String pubYn;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardQna {
        private Long id;
        private String qnaType;
        private String qstTitle;
        private String qstContent;
        private String answerContent;
    }
}

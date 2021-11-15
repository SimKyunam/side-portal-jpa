package com.mile.portal.rest.user.model.dto;

import com.mile.portal.rest.user.model.domain.BoardFaq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ReqBoard {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class BoardFaq {
        private Long id;
        private String faqType;
        private String title;
        private String content;
    }
}

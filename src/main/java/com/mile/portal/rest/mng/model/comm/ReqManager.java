package com.mile.portal.rest.mng.model.comm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

public class ReqManager {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Qna {
        private Long id;

        @NotBlank
        private String qnaType;

        @NotBlank
        private String mailSendYn;

        @NotBlank
        private Long managerId;
    }
}

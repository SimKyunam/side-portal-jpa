package com.mile.portal.rest.mng.model.comm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class ReqManager {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Qna {
        private Long id;

        @NotBlank
        private String qnaType;

        private String mailSendYn = "Y";

        @Positive(message = "managerId 값은 0보다 커야합니다.")
        private Long managerId;
    }
}

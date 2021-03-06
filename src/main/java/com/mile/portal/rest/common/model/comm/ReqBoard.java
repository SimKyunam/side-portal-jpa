package com.mile.portal.rest.common.model.comm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReqBoard {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardFaq {
        private Long id;

        @ColumnDefault("ETC")
        private String faqType;

        @Size(max = 50)
        @NotBlank
        private String title;

        @NotBlank
        private String content;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardNotice {
        private Long id;
        @NotBlank
        private String ntcType;

        @Size(max = 50)
        @NotBlank
        private String title;

        @NotBlank
        private String content;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime beginDate;

        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime endDate;

        private String hotYn;
        private String pubYn;

        //첨부 파일
        private String fileModifiedYn = "N";
        private List<String> deleteUploadNames = new ArrayList<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardQna {
        private Long id;
        private String qnaType;

        @Size(max = 50)
        @NotBlank
        private String title;

        @NotBlank
        private String content;

        //첨부 파일
        private String fileModifiedYn = "N";
        private List<String> deleteUploadNames = new ArrayList<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardQnaAnswer {
        private Long id;

        @NotBlank
        private String answerContent;

        //첨부 파일
        private String fileModifiedYn = "N";
        private List<String> deleteUploadNames = new ArrayList<>();
    }
}

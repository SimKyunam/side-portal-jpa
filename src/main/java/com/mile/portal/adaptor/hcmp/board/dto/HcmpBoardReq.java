package com.mile.portal.adaptor.hcmp.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class HcmpBoardReq {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardNotice {
        private Integer boardId;
        private String title;
        private String content;
        private String ntcType;
        private String beginDate;
        private String endDate;
        private String hotYn;
        private String pubYn;

        //업로드용
        private List<MultipartFile> files;

        //수정용
        private List<String> nameUps;
        private String fileModifiedYn = "N";
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardFaq {
        private Integer boardId;
        private String title;
        private String content;
        private String faqType;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardQna {
        private Integer boardId;
        private String title;
        private String content;

        private String qnaType;
        private String phone;
        private String email;
        private String aTitle;
        private String aContent;

        //업로드용
        private List<MultipartFile> files;

        //수정용
        private List<String> nameUps;
        private String fileModifiedYn = "N";

        public MultiValueMap<String, HttpEntity<?>> toMultiValueMap() {
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            if (boardId != null && boardId > 0)
                builder.part("boardId", boardId);
            if (title != null && !title.isEmpty())
                builder.part("title", title);
            if (content != null && !content.isEmpty())
                builder.part("content", content);
            if (qnaType != null && !qnaType.isEmpty())
                builder.part("qnaType", qnaType);
            if (phone != null && !phone.isEmpty())
                builder.part("phone", phone);
            if (email != null && !email.isEmpty())
                builder.part("email", email);
            if (aTitle != null && !aTitle.isEmpty())
                builder.part("aTitle", aTitle);
            if (aContent != null && !aContent.isEmpty())
                builder.part("aContent", aContent);

            return builder.build();
        }
    }
}

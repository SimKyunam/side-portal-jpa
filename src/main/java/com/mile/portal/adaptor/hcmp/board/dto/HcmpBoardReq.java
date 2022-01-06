package com.mile.portal.adaptor.hcmp.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class HcmpBoardReq {

    @Data
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardFaq {
        private Integer boardId;
        private String title;
        private String content;
        private String faqType;
    }

    @Data
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

        public MultiValueMap<String, Object> toMultiValueMap() {
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();

            map.add("boardId", boardId);
            map.add("title", title);
            map.add("content", content);
            map.add("qnaType", qnaType);
            map.add("phone", phone);
            map.add("email", email);
            map.add("aTitle", aTitle);
            map.add("aContent", aContent);

            return map;
        }
    }
}

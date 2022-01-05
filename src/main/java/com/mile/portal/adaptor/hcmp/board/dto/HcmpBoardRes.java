package com.mile.portal.adaptor.hcmp.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


public class HcmpBoardRes {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardNotice {
        //기본정보
        private Integer id;
        private String ntcType;
        private String ntcTypeNm;
        private String title;
        private String content;
        private Integer readCnt;
        private String beginDate;
        private String endDate;
        private String hotYn;
        private String pubYn;
        private Integer managerId;

        private String created;
        private String updated;
        private String deleted;

        //추가정보
        private String manName;
        private Integer fileCnt;
        private List<BoardAttachFile> fileInfos;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardQna {
        private Integer id;
        private String qnaType;
        private String qnaTypeNm;
        private String qTitle;
        private String qContent;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String qPhone;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String qEmail;

        private Integer userId;

        @JsonIgnore
        private String aTitle;

        private String aContent;
        private Integer managerId;

        private String created;
        private String updated;
        private String deleted;

        //추가정보
        private String loginId;
        private String userName;
        private String manName;

        private Integer qFileCnt;
        private Integer aFileCnt;

        private List<BoardAttachFile> qFileInfos;
        private List<BoardAttachFile> aFileInfos;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardAttachFile {
        private Integer id;
        private String bbsType;
        private Integer bbsId;
        private String nameDn;
        private String nameUp;

        @JsonIgnore
        private String path;

        private Long size;

        private String created;

        //추가 정보 (수정시 사용)
        @JsonIgnore
        private List<String> nameUps;
        @JsonIgnore
        private List<String> deleteNameUps;
    }
}

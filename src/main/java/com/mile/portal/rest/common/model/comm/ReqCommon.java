package com.mile.portal.rest.common.model.comm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public class ReqCommon {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserLogin {
        @NotBlank
        private String loginId;
        @NotBlank
        private String loginPwd;

        private Long userId;
        private String userName;
        private String userType;
        private String activeYn;
        private String icisNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SsoLogin {
        private String loginEmail;
        private String userName;
        private String icisNo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        private String providerCd;
        private String resourceCd;
        private Integer connectionId;
        private String resourceId;
        private String resourceName;
        private String tagKey;
        private String tagValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyInfo {
        private Integer exchangeDay;
        private String smsKey;
        private String smsSecret;
        private String smsId;
        private String smsPwd;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Code {
        private Long codeId;
        private String codeCd;
        private String codeValue;
        private int ord;
        private Long parentId;
    }
}

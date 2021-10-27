package com.mile.portal.rest.common.model.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class ReqCommon {

    @Data
    @Builder
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
    public static class SsoLogin {
        private String loginEmail;
        private String userName;
        private String icisNo;
    }

    @Data
    @Builder
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
    public static class MyInfo {
        private Integer exchangeDay;
        private String smsKey;
        private String smsSecret;
        private String smsId;
        private String smsPwd;
    }
}

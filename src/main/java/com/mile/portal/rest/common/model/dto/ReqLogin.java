package com.mile.portal.rest.common.model.dto;

import com.mile.portal.rest.common.model.enums.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReqLogin {

    @NotBlank
    private String loginId;
    @NotBlank
    private String loginPwd;

    private Long userId;
    private String userName;
    private Authority userType;
    private String activeYn;
    private String icisNo;
}

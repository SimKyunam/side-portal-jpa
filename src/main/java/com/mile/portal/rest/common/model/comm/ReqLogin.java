package com.mile.portal.rest.common.model.comm;

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
    private String icisNo;
    private String status;

    private String phone;
    private String email;
    private String emailPassYn;
}

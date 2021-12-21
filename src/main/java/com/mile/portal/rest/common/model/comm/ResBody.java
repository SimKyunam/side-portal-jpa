package com.mile.portal.rest.common.model.comm;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
public class ResBody implements Serializable {
    private static final long serialVersionUID = -806369983640789120L;

    public static final String CODE_SUCCESS = "success";
    public static final String CODE_ERROR = "error";

    private String retCode;
    private String errMsg;
    private Object data;

    public ResBody(String retCode, String errMsg, Object data) {
        this.retCode = retCode;
        this.errMsg = errMsg;
        this.data = data;
    }
}

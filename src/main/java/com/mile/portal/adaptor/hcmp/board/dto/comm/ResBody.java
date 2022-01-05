package com.mile.portal.adaptor.hcmp.board.dto.comm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResBody<T> {
    private String retCode;
    private String errMsg;
    private T data;
}

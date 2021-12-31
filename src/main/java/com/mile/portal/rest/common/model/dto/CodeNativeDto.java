package com.mile.portal.rest.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeNativeDto implements Serializable {
    private String code;
    private String codeName;
    private String codeValue;
    private int ord;
    private int depth;
    private Long childCount;
}

package com.mile.portal.rest.common.model.dto;

import com.mile.portal.rest.common.model.domain.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeNativeDto {
    private String code;
    private String codeName;
    private String codeValue;
    private int ord;
    private int depth;
    private Long childCount;
}
package com.mile.portal.rest.common.model.dto;

import com.mile.portal.rest.base.dto.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeNativeDto extends BaseDto {
    private String code;
    private String codeName;
    private String codeValue;
    private int ord;
    private int depth;
    private Long childCount;
}

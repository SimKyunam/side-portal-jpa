package com.mile.portal.rest.common.model.dto;

import com.mile.portal.rest.base.dto.BaseDto;
import com.mile.portal.rest.common.model.domain.Code;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeDto extends BaseDto {
    private String code;
    private String codeName;
    private String codeValue;
    private int ord;
    private int depth;

    private List<Code> child = new ArrayList<>();
    private Long childCount;
}

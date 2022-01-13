package com.mile.portal.rest.mng.model.dto;

import com.mile.portal.rest.base.dto.BaseDto;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManagerQnaDto extends BaseDto {
    private Long id;

    private String qnaType;

    private String qnaTypeName;

    private String mailSendYn;

    private Long managerId;

    private String managerName;
}

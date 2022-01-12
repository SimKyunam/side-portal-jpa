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

    private Long managerId;
    private Long managerName;
}

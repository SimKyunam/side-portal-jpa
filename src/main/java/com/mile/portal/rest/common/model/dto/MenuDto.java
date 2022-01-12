package com.mile.portal.rest.common.model.dto;

import com.mile.portal.rest.base.dto.BaseDto;
import com.mile.portal.rest.common.model.domain.Menu;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto extends BaseDto {
    private Long id;
    private String menuName;
    private String menuValue;
    private int ord;
    private int depth;

    private List<Menu> child = new ArrayList<>();
    private Long childCount;
}

package com.mile.portal.rest.common.model.dto;

import com.mile.portal.rest.common.model.domain.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto implements Serializable {
    private Long id;
    private String menuName;
    private String menuValue;
    private int ord;
    private int depth;

    private List<Menu> child = new ArrayList<>();
    private Long childCount;
}

package com.yongy.dotori.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CategoryGroupListDTO {
    private String groupTitle;
    private List<ActiveCategoryDTO> activeCategoryDTOList;
}

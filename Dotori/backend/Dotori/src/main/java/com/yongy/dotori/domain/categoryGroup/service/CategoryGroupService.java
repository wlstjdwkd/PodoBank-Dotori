package com.yongy.dotori.domain.categoryGroup.service;

import com.yongy.dotori.domain.categoryGroup.dto.CategoryGroupDTO;

import java.util.List;

public interface CategoryGroupService {
    List<CategoryGroupDTO> findAllCategoryGroup();
}

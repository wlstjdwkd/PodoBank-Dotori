package com.yongy.dotori.domain.category.service;

import com.yongy.dotori.domain.category.dto.CategoryDetailDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDetailDTO> findAllCategory();
}


package com.yongy.dotori.domain.category.service;

import com.yongy.dotori.domain.category.dto.CategoryDTO;
import com.yongy.dotori.domain.category.entity.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAllCategory();
}


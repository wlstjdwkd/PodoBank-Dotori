package com.yongy.dotori.domain.categoryGroup.service;


import com.yongy.dotori.domain.category.repository.CategoryRepository;
import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import com.yongy.dotori.domain.categoryGroup.repository.CategoryGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryGroupServiceImpl implements CategoryGroupService {

    private final CategoryGroupRepository categoryGroupRepository;

    @Override
    public void createCategoryGroup(String title) {
        categoryGroupRepository.save(CategoryGroup.builder()
                        .groupTitle(title).build());
    }
}

package com.yongy.dotori.domain.categoryGroup.repository;

import com.yongy.dotori.domain.categoryGroup.entity.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    CategoryGroup findByCategoryGroupSeq(Long categoryGroupSeq);
}

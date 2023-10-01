package com.yongy.dotorimainservice.domain.categoryGroup.repository;


import com.yongy.dotorimainservice.domain.categoryGroup.entity.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryGroupRepository extends JpaRepository<CategoryGroup, Long> {
    CategoryGroup findByCategoryGroupSeq(Long categoryGroupSeq);
    List<CategoryGroup> findAllByUserUserSeq(Long userSeq);
}
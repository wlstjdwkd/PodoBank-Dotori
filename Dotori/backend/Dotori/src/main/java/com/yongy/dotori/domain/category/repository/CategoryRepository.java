package com.yongy.dotori.domain.category.repository;

import com.yongy.dotori.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserUserSeq(Long userSeq);
}

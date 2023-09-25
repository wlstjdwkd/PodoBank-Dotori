package com.yongy.dotori.domain.category.repository;

import com.yongy.dotori.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserUserSeq(Long userSeq);
    Category findByCategorySeq(Long categorySeq);
}

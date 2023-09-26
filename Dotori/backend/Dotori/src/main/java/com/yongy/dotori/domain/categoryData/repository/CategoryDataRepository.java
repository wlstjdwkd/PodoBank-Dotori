package com.yongy.dotori.domain.categoryData.repository;

import com.yongy.dotori.domain.categoryData.entity.CategoryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDataRepository extends JpaRepository<CategoryData, Long> {
    List<CategoryData> findAllByCategoryCategorySeq(String categorySeq);
    CategoryData findByDataCode(String code);
    int deleteByCategoryDataSeq(String categoryDataSeq);
}

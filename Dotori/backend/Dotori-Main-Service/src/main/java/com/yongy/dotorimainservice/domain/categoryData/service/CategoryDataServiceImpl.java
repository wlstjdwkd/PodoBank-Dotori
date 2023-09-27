package com.yongy.dotorimainservice.domain.categoryData.service;


import com.yongy.dotori.domain.categoryData.entity.CategoryData;
import com.yongy.dotori.domain.categoryData.repository.CategoryDataRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
public class CategoryDataServiceImpl {

    @Autowired
    private CategoryDataRepository categoryDataRepository;

    public List<CategoryData> getCategoryDataList(String categorySeq){
        return categoryDataRepository.findAllByCategoryCategorySeq(categorySeq);
    }
}

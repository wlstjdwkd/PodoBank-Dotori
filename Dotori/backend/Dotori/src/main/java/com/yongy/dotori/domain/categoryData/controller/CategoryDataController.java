package com.yongy.dotori.domain.categoryData.controller;

import com.yongy.dotori.domain.categoryData.entity.CategoryData;
import com.yongy.dotori.domain.categoryData.repository.CategoryDataRepository;
import com.yongy.dotori.global.common.BaseResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/categoryData")
public class CategoryDataController {

    @Autowired
    private CategoryDataRepository categoryDataRepository;

    @GetMapping()
    public ResponseEntity<? extends BaseResponseBody> getCategoryDataList(@RequestParam String categorySeq){
        List<CategoryData> categoryDataList = categoryDataRepository.findAllByCategoryCategorySeq(categorySeq);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, categoryDataList));
    }

    @DeleteMapping()
    public ResponseEntity<? extends BaseResponseBody> deleteCategoryData(@RequestParam String categoryDataSeq){
        categoryDataRepository.deleteByCategoryDataSeq(categoryDataSeq);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "success"));
    }

    //TODO : 데이터의 count 증가
}

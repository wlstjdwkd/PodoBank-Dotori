package com.yongy.dotori.domain.categoryData.controller;

import com.yongy.dotori.domain.category.service.CategoryService;
import com.yongy.dotori.domain.categoryData.dto.response.CategoryDataResDto;
import com.yongy.dotori.domain.categoryData.entity.CategoryData;
import com.yongy.dotori.domain.categoryData.repository.CategoryDataRepository;
import com.yongy.dotori.domain.categoryData.service.CategoryDataService;
import com.yongy.dotori.global.common.BaseResponseBody;
import io.swagger.v3.oas.annotations.Operation;
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
    private CategoryDataService categoryDataService;

    // NOTE : 카테고리 데이터 조회
    @Operation(summary = "카테고리 데이터 조회", description = "USER")
    @GetMapping()
    public ResponseEntity<List<CategoryDataResDto>> findCategoryData(@RequestParam Long categorySeq){
        return ResponseEntity.ok(categoryDataService.findCategoryData(categorySeq));
    }

    // NOTE : 카테고리 데이터 삭제
    @Operation(summary = "카테고리 데이터 삭제", description = "USER")
    @DeleteMapping()
    public ResponseEntity<Void> deleteCategoryData(@RequestBody String dataCode){
        categoryDataService.deleteByDataCode(dataCode);
        return ResponseEntity.ok().build();
    }

}

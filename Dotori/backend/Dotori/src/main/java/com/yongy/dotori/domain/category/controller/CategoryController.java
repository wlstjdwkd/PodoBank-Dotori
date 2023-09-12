package com.yongy.dotori.domain.category.controller;

import com.yongy.dotori.domain.category.dto.CategoryDTO;
import com.yongy.dotori.domain.category.entity.Category;
import com.yongy.dotori.domain.category.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/v1/category")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    public CategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }


//    @Operation(summary = "카테고리 등록")
//    @PostMapping("/")
//    public ResponseEntity<Void> createCategory(){
//
//
//        return ResponseEntity.ok().build();
//    }


    @Operation(summary = "카테고리 리스트")
    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> findAllCategoryTitle(){
        List<CategoryDTO> categoryTitles = categoryService.findAllCategory();
        return ResponseEntity.ok(categoryTitles);
    }
}

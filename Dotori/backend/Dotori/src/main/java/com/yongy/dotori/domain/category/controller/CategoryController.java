package com.yongy.dotori.domain.category.controller;

import com.yongy.dotori.domain.category.dto.CategoryDetailDTO;
import com.yongy.dotori.domain.category.service.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/category")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @Operation(summary = "카테고리 리스트")
    @GetMapping("/")
    public ResponseEntity<List<CategoryDetailDTO>> findAllCategoryTitle(){
        List<CategoryDetailDTO> categoryTitles = categoryService.findAllCategory();
        return ResponseEntity.ok(categoryTitles);
    }
}

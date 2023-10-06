package com.yongy.dotori.domain.categoryGroup.controller;

import com.yongy.dotori.domain.categoryGroup.dto.CategoryGroupDTO;
import com.yongy.dotori.domain.categoryGroup.repository.CategoryGroupRepository;
import com.yongy.dotori.domain.categoryGroup.service.CategoryGroupService;
import com.yongy.dotori.domain.categoryGroup.service.CategoryGroupServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/categoryGroup")
public class CategoryGroupController {

    private final CategoryGroupServiceImpl categoryGroupService;

    @Operation(summary = "카테고리 그룹 리스트 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "카테고리 그룹 리스트 조회 성공")
    })
    @GetMapping("/")
    public ResponseEntity<List<CategoryGroupDTO>> findAllCategoryGroup(){
        List<CategoryGroupDTO> result = categoryGroupService.findAllCategoryGroup();
        return ResponseEntity.ok(result);
    }

}

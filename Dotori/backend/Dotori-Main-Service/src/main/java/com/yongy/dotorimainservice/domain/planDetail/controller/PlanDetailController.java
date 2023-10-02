package com.yongy.dotorimainservice.domain.planDetail.controller;


import com.yongy.dotorimainservice.domain.plan.service.PlanService;
import com.yongy.dotorimainservice.domain.planDetail.dto.response.PlanDetailListResDto;
import com.yongy.dotorimainservice.domain.planDetail.service.PlanDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/planDetail")
public class PlanDetailController {

    @Autowired
    private PlanDetailService planDetailService;

    @Autowired
    private PlanService planService;

    @Operation(summary = "명세서 상세 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "명세서 상세 조회 성공")
    })
    @GetMapping("/specification")
    public ResponseEntity<List<PlanDetailListResDto>> getPlanDetail(Long planSeq){
        List<PlanDetailListResDto> planDetailList = planDetailService.getPlanDetail(planSeq);
        return ResponseEntity.ok().body(planDetailList);
    }

}

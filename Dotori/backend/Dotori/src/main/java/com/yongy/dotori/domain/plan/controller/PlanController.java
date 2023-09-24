package com.yongy.dotori.domain.plan.controller;

import com.yongy.dotori.domain.plan.dto.PlanDTO;
<<<<<<< HEAD
import com.yongy.dotori.domain.plan.service.PlanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
=======
import com.yongy.dotori.domain.plan.dto.SavingDTO;
import com.yongy.dotori.domain.plan.service.PlanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
>>>>>>> a5a17dab00283bd4a94fa90d0d08d37e87050a77
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plan")
public class PlanController {

    private final PlanServiceImpl planService;

<<<<<<< HEAD
    @Operation(summary = "새로운 계획 등록")
    @PostMapping("/")
=======
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "계획 생성 성공")
    })
    @Operation(summary = "새로운 계획 등록")
    @PostMapping("")
>>>>>>> a5a17dab00283bd4a94fa90d0d08d37e87050a77
    public ResponseEntity<Void> createPlan(@RequestBody PlanDTO planDTO){
        planService.createPlan(planDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "실행중인 계획 중단")
<<<<<<< HEAD
=======
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "계획 중단 성공")
    })
>>>>>>> a5a17dab00283bd4a94fa90d0d08d37e87050a77
    @GetMapping("/stop/{planSeq}")
    public ResponseEntity<Void> terminatePlan(@PathVariable Long planSeq){
        planService.terminatePlan(planSeq);
        return ResponseEntity.ok().build();
    }
<<<<<<< HEAD
=======

    @Operation(summary = "저축하기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "저축 성공")
    })
    @GetMapping("/saving")
    public ResponseEntity<Void> saving(@RequestBody SavingDTO savingDTO) {
        planService.saving(savingDTO);
        return ResponseEntity.ok().build();
    }
>>>>>>> a5a17dab00283bd4a94fa90d0d08d37e87050a77
}

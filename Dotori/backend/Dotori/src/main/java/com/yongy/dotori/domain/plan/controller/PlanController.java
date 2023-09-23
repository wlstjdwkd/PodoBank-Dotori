package com.yongy.dotori.domain.plan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.plan.dto.PlanDTO;
import com.yongy.dotori.domain.plan.dto.SavingDTO;
import com.yongy.dotori.domain.plan.service.PlanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "새로운 계획 등록")
    @PostMapping("")
    public ResponseEntity<Void> createPlan(@RequestBody PlanDTO planDTO){
        planService.createPlan(planDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "실행중인 계획 중단")
    @GetMapping("/stop/{planSeq}")
    public ResponseEntity<Void> terminatePlan(@PathVariable Long planSeq){
        planService.terminatePlan(planSeq);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "저축하기")
    @GetMapping("/saving")
    public ResponseEntity<Void> saving(@RequestBody SavingDTO savingDTO) {
        planService.saving(savingDTO);
        return ResponseEntity.ok().build();
    }
}

package com.yongy.dotori.domain.plan.controller;

import com.yongy.dotori.domain.plan.service.PlanServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plan")
public class PlanController {

    private final PlanServiceImpl planService;


    @Operation(summary = "새로운 계획 등록")
    @PostMapping("/")
    public ResponseEntity<Void> createPlan(){

        return ResponseEntity.ok().build();
    }
}

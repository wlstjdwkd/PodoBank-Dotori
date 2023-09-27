package com.yongy.dotorimainservice.domain.plan.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yongy.dotori.domain.plan.dto.ActivePlanDTO;
import com.yongy.dotori.domain.plan.dto.PlanDTO;
import com.yongy.dotori.domain.plan.dto.PlanStateDTO;
import com.yongy.dotori.domain.plan.dto.SavingDTO;
import com.yongy.dotori.domain.plan.dto.response.PlanListDto;
import com.yongy.dotori.domain.plan.service.PlanService;
import com.yongy.dotori.domain.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/plan")
public class PlanController {

    @Autowired
    private PlanService planService;


    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "계획 생성 성공")
    })
    @Operation(summary = "새로운 계획 등록")
    @PostMapping()
    public ResponseEntity<Void> createPlan(@RequestBody PlanDTO planDTO){
        planService.createPlan(planDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "실행중인 계획 중단")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "계획 중단 성공")
    })
    @PatchMapping("/stop/{planSeq}")
    public ResponseEntity<Void> terminatePlan(@PathVariable Long planSeq){
        planService.terminatePlan(planSeq);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "저축하기")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "저축 성공")
    })

    @PostMapping("/saving")
    public ResponseEntity<Void> saving(@RequestBody SavingDTO savingDTO) throws JsonProcessingException {
        planService.saving(savingDTO);
        return ResponseEntity.ok().build();
    }

    // NOTE : 사용자의 명세서 전체 불러오기
    @GetMapping("/specification")
    public ResponseEntity<List<PlanListDto>> planList(){
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // TODO : Completed 또는 Saved
        List<PlanListDto> planListDto = planService.getPlanList(user.getUserSeq());

        return ResponseEntity.ok().body(planListDto);
    }


    @Operation(summary = "계획 상태 변경")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "COMPLETED 상태 변경 성공")
    })
    @PatchMapping("/complete")
    public ResponseEntity<Void> updateState(@RequestBody PlanStateDTO planStateDTO){
        planService.updateState(planStateDTO);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "계좌에 연결된 실행중인 계획 조회")
    @GetMapping("/{accountSeq}")
    public ResponseEntity<ActivePlanDTO> findAllPlan(@PathVariable Long accountSeq) throws JsonProcessingException {
        ActivePlanDTO result = planService.findAllPlan(accountSeq);
        return ResponseEntity.ok(result);
    }
}

package com.yongy.dotori.domain.planDetail.controller;


import com.yongy.dotori.domain.category.repository.CategoryRepository;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.service.PlanService;
import com.yongy.dotori.domain.planDetail.dto.response.PlanDetailListResDto;
import com.yongy.dotori.domain.planDetail.entity.PlanDetail;
import com.yongy.dotori.domain.planDetail.service.PlanDetailService;
import kotlinx.serialization.Required;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/v1/planDetail")
public class PlanDetailController {

    @Autowired
    private PlanDetailService planDetailService;

    @Autowired
    private PlanService planService;


    @GetMapping("/specification")
    public ResponseEntity<List<PlanDetailListResDto>> getPlanDetail(Long planSeq){
        List<PlanDetailListResDto> planDetailList = planDetailService.getPlanDetail(planSeq);
        return ResponseEntity.ok().body(planDetailList);
    }
}

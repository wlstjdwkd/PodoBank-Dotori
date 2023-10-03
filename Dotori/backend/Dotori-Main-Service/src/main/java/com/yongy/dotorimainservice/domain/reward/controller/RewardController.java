package com.yongy.dotorimainservice.domain.reward.controller;

import com.yongy.dotorimainservice.domain.reward.dto.DotoriDTO;
import com.yongy.dotorimainservice.domain.reward.service.RewardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reward")
public class RewardController {
    private final RewardService rewardService;

    @Operation(summary = "도토리 개수 조회")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200", description = "도토리 조회 성공"),
            @ApiResponse(responseCode = "404", description = "리워드 정보 없음")
    })
    @GetMapping()
    ResponseEntity<DotoriDTO> getUserDotori(){
        return ResponseEntity.ok(rewardService.getDotori());
    }


    // ----------통신----------
    // NOTE : 사용자 리워드 등록
    @GetMapping("/communication/enroll")
    public ResponseEntity<String> createUserReward(@RequestParam Long userSeq){
        rewardService.createUserReward(userSeq);
        return ResponseEntity.ok().build();
    }

    // NOTE : 사용자 리워드 삭제
    @GetMapping("/communication/delete")
    public ResponseEntity<String> deleteUserReward(@RequestParam Long userSeq){
        rewardService.deleteUserReward(userSeq);
        return ResponseEntity.ok().build();
    }
}

package com.yongy.dotorimainservice.domain.reward.controller;

import com.yongy.dotorimainservice.domain.reward.service.RewardService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Data
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/reward")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/enroll")
    public ResponseEntity<String> createUserReward(@RequestParam Long userSeq){

    }
}

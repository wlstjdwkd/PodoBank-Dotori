package com.yongy.dotorimainservice.domain.reward.service;

import com.yongy.dotorimainservice.domain.reward.entity.Reward;
import com.yongy.dotorimainservice.domain.reward.repository.RewardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
public class RewardServiceImpl {

    @Autowired
    private RewardRepository rewardRepository;
//
//    public void createUserReward(Long userSeq){
//        Reward reward = Reward.
//    }
}

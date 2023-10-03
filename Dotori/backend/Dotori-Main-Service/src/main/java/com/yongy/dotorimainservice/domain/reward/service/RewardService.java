package com.yongy.dotorimainservice.domain.reward.service;

import com.yongy.dotorimainservice.domain.reward.dto.DotoriDTO;

public interface RewardService {

    DotoriDTO getDotori();
    void createUserReward(Long userSeq);
}

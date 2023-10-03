package com.yongy.dotorimainservice.domain.reward.service;

import com.yongy.dotorimainservice.domain.reward.dto.DotoriDTO;
import com.yongy.dotorimainservice.domain.reward.entity.Reward;
import com.yongy.dotorimainservice.domain.reward.exception.NotfoundRewardException;
import com.yongy.dotorimainservice.domain.reward.repository.RewardRepository;
import com.yongy.dotorimainservice.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RewardServiceImpl implements RewardService{

    private final RewardRepository rewardRepository;
    @Override
    public DotoriDTO getDotori() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Reward reward = rewardRepository.findByUserSeq(user.getUserSeq());

        if(reward == null){
            throw new NotfoundRewardException("Reward 정보가 존재하지 않습니다.");
        }

        return DotoriDTO.builder().dotori(reward.getDotori()).build();
    }
}

package com.yongy.dotoriuserservice.domain.user.service;


import com.yongy.dotoriuserservice.domain.account.entity.Account;
import com.yongy.dotoriuserservice.domain.account.repository.AccountRepository;
import com.yongy.dotoriuserservice.domain.plan.entity.Plan;
import com.yongy.dotoriuserservice.domain.plan.entity.State;
import com.yongy.dotoriuserservice.domain.plan.repository.PlanRepository;
import com.yongy.dotoriuserservice.domain.purpose.entity.Purpose;
import com.yongy.dotoriuserservice.domain.purpose.repository.PurposeRepository;
import com.yongy.dotoriuserservice.domain.user.entity.User;
import com.yongy.dotoriuserservice.domain.user.repository.UserRepository;
import com.yongy.dotoriuserservice.global.redis.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;


    // NOTE : 사용자를 저장한다.
    public void saveUser(User user){
        userRepository.save(user);
    }


    // NOTE : 사용자의 RefreshToken에 맞는 UserRefreshToken을 삭제한다.
    public void deleteUserRefreshToken(String id){
        userRefreshTokenRepository.deleteById(id);
    }

    // NOTE : 목표 계좌에 남아있는 전체 금액을 반환한다.
    public BigDecimal totalPurposeMoney(String id){
        List<Purpose> purposeList = purposeRepository.findAllByUserIdAndTerminatedAtIsNull(id);
        BigDecimal result = BigDecimal.ZERO;
        for(Purpose purpose : purposeList){
            result = result.add(purpose.getCurrentBalance());
        }
        return result;
    }


    // NOTE : 사용자의 계좌 모두 삭제하기
    public void removeUserAccounts(Long userSeq){
        List<Account> accountList = accountRepository.findAllByUserSeqAndDeleteAtIsNull(userSeq);
        for(Account account : accountList){
            account.setDeleteAt(null);
        }
        accountRepository.saveAll(accountList);
    }

    // NOTE : 사용자의 진행중인 계획 모두 삭제하기
    public void removeUserPlans(Long userSeq){
        List<Plan> planList = planRepository.findAllByUserSeqAndTerminatedAtIsNull(userSeq);
        for(Plan plan : planList){
            plan.setPlanState(State.INACTIVE);
            plan.setEndAt(LocalDateTime.now());
        }
        planRepository.saveAll(planList);
    }

    // NOTE : 사용자 탈퇴하기
    public void removeRetireUser(User user){
        user.setExpiredAt(LocalDateTime.now());
        this.saveUser(user);
    }

    // TODO : DB에서 사용자의 정보를 가져온다.
    public User getUserFromDB(Long userSeq){
        return userRepository.findByUserSeqAndExpiredAtIsNull(userSeq);
    }
}

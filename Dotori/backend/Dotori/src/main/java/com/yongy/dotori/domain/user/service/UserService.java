package com.yongy.dotori.domain.user.service;

import com.yongy.dotori.domain.account.entity.Account;
import com.yongy.dotori.domain.account.repository.AccountRepository;
import com.yongy.dotori.domain.plan.entity.Plan;
import com.yongy.dotori.domain.plan.entity.State;
import com.yongy.dotori.domain.plan.repository.PlanRepository;
import com.yongy.dotori.domain.purpose.entity.Purpose;
import com.yongy.dotori.domain.purpose.repository.PurposeRepository;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.global.email.EmailUtil;

import com.yongy.dotori.global.redis.entity.EmailAuth;
import com.yongy.dotori.global.redis.entity.UserRefreshToken;
import com.yongy.dotori.global.redis.repository.EmailAuthRepository;
import com.yongy.dotori.global.redis.repository.PersonalAuthRepository;

import com.yongy.dotori.global.redis.repository.UserRefreshTokenRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @Autowired
    private PersonalAuthRepository personalAuthRepository;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    private UserRefreshTokenRepository userRefreshTokenRepository;

    @Autowired
    private PurposeRepository purposeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PlanRepository planRepository;

    // NOTE : 이메일 인증코드를 생성한다.
    public void emailCertification(String id){
        Random random = new Random();
        String authCode = String.valueOf(random.nextInt(8888) + 1111); // 1111 ~ 9999의 랜덤한 숫자
        sendEmailAuthCode(id, authCode);
    }

    // NOTE : 인증코드를 사용자 이메일로 전송한다.
    public void sendEmailAuthCode(String id, String authCode){
        emailUtil.setSubject("도토리의 인증번호");
        emailUtil.setPrefix("회원 가입을 위한 인증번호는 ");
        emailUtil.sendEmailAuthCode(id, authCode);
        emailAuthRepository.save(EmailAuth.of(id, authCode));
    }


    // NOTE : EmailAuth 반환
    public EmailAuth getEmailAuth(String authCode){
        return emailAuthRepository.findByAuthCode(authCode);
    }

    // NOTE : EmailAuth 삭제
    public void deleteEmailAuth(String email){
        emailAuthRepository.deleteById(email);
    }

    // NOTE : 사용자를 가져온다.
    public User getUser(String id){
        return userRepository.findByIdAndExpiredAtIsNull(id);
    }

    // NOTE : 사용자를 저장한다.
    public void saveUser(User user){
        userRepository.save(user);
    }

    // NOTE : 사용자의 RefreshToken을 RedisDB에 저장한다.
    public void saveUserRefreshToken(UserRefreshToken userRefreshToken){
        userRefreshTokenRepository.save(userRefreshToken);
    }

    // NOTE : 사용자의 RefreshToken에 맞는 UserRefreshToken을 가져온다.
    public UserRefreshToken getUserRefreshToken(String refreshToken){
        return userRefreshTokenRepository.findByRefreshToken(refreshToken);
    }

    // NOTE : 사용자의 RefreshToken에 맞는 UserRefreshToken을 삭제한다.
    public void deleteUserRefreshToken(String id){
        userRefreshTokenRepository.deleteById(id);
    }

    // NOTE : 목표 계좌에 남아있는 전체 금액을 반환한다.
    public BigDecimal totalPurposeMoney(String id){
        List<Purpose> purposeList = purposeRepository.findAllByUserId(id);
        BigDecimal result = BigDecimal.ZERO;
        for(Purpose purpose : purposeList){
            result = result.add(purpose.getCurrentBalance());
        }
        return result;
    }


    // NOTE : 사용자의 계좌 모두 삭제하기
    public void removeUserAccounts(Long userSeq){
        List<Account> accountList = accountRepository.findAllByUserUserSeqAndDeleteAtIsNull(userSeq);
        for(Account account : accountList){
            account.setDeleteAt(null);
        }
        accountRepository.saveAll(accountList);
    }

    // NOTE : 사용자의 진행중인 계획 모두 삭제하기
    public void removeUserPlans(Long userSeq){
        List<Plan> planList = planRepository.findAllByUserUserSeqAndTerminatedAtIsNull(userSeq);
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
}

package com.yongy.dotori.domain.user.service;

import com.yongy.dotori.global.email.EmailUtil;

import com.yongy.dotori.global.redis.entity.EmailAuth;
import com.yongy.dotori.global.redis.repository.EmailAuthRepository;
import com.yongy.dotori.global.redis.repository.PersonalAuthRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private EmailAuthRepository emailAuthRepository;
    @Autowired
    private PersonalAuthRepository personalAuthRepository;
    @Autowired
    private EmailUtil emailUtil;

    public void emailCert(String id){
        Random random = new Random();
        String authCode = String.valueOf(random.nextInt(8888) + 1111); // 1111 ~ 9999의 랜덤한 숫자

        sendEmailAuthCode(id, authCode);
    }

    public void sendEmailAuthCode(String id, String authCode){
        emailUtil.setSubject("도토리의 인증번호");
        emailUtil.setPrefix("회원 가입을 위한 인증번호는 ");
        emailUtil.sendEmailAuthCode(id, authCode);
        emailAuthRepository.save(EmailAuth.of(authCode, id));
    }

    public String getEmailAuthId(String authCode){
        Optional<EmailAuth> emailAuth = emailAuthRepository.findById(authCode);
        if(emailAuth != null)
            return emailAuth.get().getId();
        return null;
    }

    public void deleteEmailAuthCode(String authCode){
        emailAuthRepository.deleteById(authCode);
    }

}

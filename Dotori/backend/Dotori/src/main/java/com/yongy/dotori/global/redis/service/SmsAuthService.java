package com.yongy.dotori.global.redis.service;

import com.yongy.dotori.global.redis.entity.SmsAuth;
import com.yongy.dotori.global.redis.repository.SmsAuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class SmsAuthService {

    @Autowired
    private SmsAuthRepository smsAuthRepository;

    public SmsAuth getSmsAuth(String codeFromUser){
        SmsAuth smsAuth = smsAuthRepository.findById(codeFromUser).orElseThrow(
                () -> new IllegalArgumentException()
        );
        return smsAuth;
    }

}

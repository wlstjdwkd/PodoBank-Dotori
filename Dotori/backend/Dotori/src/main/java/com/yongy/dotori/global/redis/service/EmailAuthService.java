package com.yongy.dotori.global.redis.service;

import com.yongy.dotori.global.redis.entity.EmailAuth;
import com.yongy.dotori.global.redis.repository.EmailAuthRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Transactional
@Service
public class EmailAuthService {

    @Autowired
    private EmailAuthRepository emailAuthRepository;

    public EmailAuth getEmailAuth(String id){
        EmailAuth emailAuth = emailAuthRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException()
        );
        return emailAuth;
    }
}

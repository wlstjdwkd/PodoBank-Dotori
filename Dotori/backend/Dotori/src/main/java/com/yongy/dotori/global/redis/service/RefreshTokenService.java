package com.yongy.dotori.global.redis.service;

import com.yongy.dotori.global.redis.repository.RefreshTokenRepository;
import com.yongy.dotori.global.redis.entity.RefreshToken;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public RefreshToken getRefreshToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findById(token).orElseThrow(
                () -> new IllegalArgumentException()
        );

        return refreshToken;
    }



}

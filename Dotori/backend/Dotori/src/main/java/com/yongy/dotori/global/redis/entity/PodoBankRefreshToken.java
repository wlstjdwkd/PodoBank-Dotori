package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="PodoBankAccessToken", timeToLive = 1000L * 60 * 60 * 24 * 6)
public class PodoBankRefreshToken {

    @Id
    private String tokenName;

    private String token;


    public PodoBankRefreshToken(String tokenName, String token) {
        this.tokenName = tokenName;
        this.token = token;
    }

    public static PodoBankRefreshToken of(String tokenName, String token){
        return new PodoBankRefreshToken(tokenName, token);
    }
}


package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="PodoBankAccessToken", timeToLive = 1000L * 60 * 3)
public class PodoBankAccessToken {

    @Id
    private String tokenName;

    private String token;


    public PodoBankAccessToken(String tokenName, String token) {
        this.tokenName = tokenName;
        this.token = token;
    }

    public static PodoBankAccessToken of(String tokenName, String token){
        return new PodoBankAccessToken(tokenName, token);
    }
}

package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="BankAccessToken", timeToLive = 1000L * 60 * 3)
public class BankAccessToken {

    @Id
    private String tokenName;

    private String token;


    public BankAccessToken(String tokenName, String token) {
        this.tokenName = tokenName;
        this.token = token;
    }

    public static BankAccessToken of(String tokenName, String token){
        return new BankAccessToken(tokenName, token);
    }
}

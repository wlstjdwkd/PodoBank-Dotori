package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value="BankAccessToken", timeToLive = 1000L * 60 * 3)
public class BankAccessToken {

    @Id
    private String bankName;

    @Indexed
    private String token;


    public BankAccessToken(String bankName, String token) {
        this.bankName = bankName;
        this.token = token;
    }

    public static BankAccessToken of(String bankName, String token){
        return new BankAccessToken(bankName, token);
    }
}

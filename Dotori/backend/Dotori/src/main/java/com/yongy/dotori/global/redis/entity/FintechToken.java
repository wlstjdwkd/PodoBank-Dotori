package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="FintechToken", timeToLive = 31536000L)
public class FintechToken {

    @Id
    private String accountNumber;

    private String fintechCode;

    public FintechToken(String accountNumber, String fintechCode) {
        this.accountNumber = accountNumber;
        this.fintechCode = fintechCode;
    }


    public static FintechToken of(String accountNumber, String fintechCode){
        return new FintechToken(accountNumber, fintechCode);
    }


}

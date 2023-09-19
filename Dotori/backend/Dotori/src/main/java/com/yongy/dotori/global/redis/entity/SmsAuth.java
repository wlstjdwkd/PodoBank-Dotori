package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="smsAuth", timeToLive = 1000L * 60 * 3)
public class SmsAuth {
    @Id
    private String authCode;

    private String id;

    public SmsAuth(String authCode, String id) {
        this.authCode = authCode;
        this.id = id;
    }


    public static SmsAuth of(String authCode, String id){
        return new SmsAuth(authCode, id);
    }
}

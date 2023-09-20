package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;



@Getter
@RedisHash(value="RefreshToken", timeToLive = 1000L * 60 * 60 * 24)
public class RefreshToken {

    @Id
    private String refreshToken;

    private String id;


    public RefreshToken(String refreshToken, String id){
        this.refreshToken = refreshToken;
        this.id = id;
    }

    public static RefreshToken of(String refreshToken, String id) {
        return new RefreshToken(refreshToken, id);
    }
}

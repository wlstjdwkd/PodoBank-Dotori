package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@RedisHash(value="UserRefreshToken", timeToLive = 1000L * 60 * 60 * 24)
public class UserRefreshToken {

    @Id
    private String email;

    @Indexed
    private String refreshToken;


    public UserRefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public static UserRefreshToken of(String email, String refreshToken) {
        return new UserRefreshToken(email, refreshToken);
    }
}

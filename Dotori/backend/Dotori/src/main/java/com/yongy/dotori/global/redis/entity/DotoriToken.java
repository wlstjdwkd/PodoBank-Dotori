package com.yongy.dotori.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value="DotoriToken")
public class DotoriToken {

    @Id
    private String tokenName;

    private String token;

    @TimeToLive
    private long expiredAt;

    public DotoriToken(String tokenName, String token, long expiredAt) {
        this.tokenName = tokenName;
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public static DotoriToken of(String tokenName, String token, long expiredAt){
        return new DotoriToken(tokenName, token , expiredAt);
    }

}

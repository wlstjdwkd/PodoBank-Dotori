package com.yongy.dotori.global.redis.entity;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="EmailAuth", timeToLive = 1000L * 60 * 5)
public class EmailAuth {

    @Id
    private String authCode;

    private String id;

    public EmailAuth(String authCode, String id) {
        this.authCode = authCode;
        this.id = id;
    }

    public static EmailAuth of(String authCode, String id) {return new EmailAuth(authCode, id);};
}

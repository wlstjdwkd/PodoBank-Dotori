package com.yongy.dotori.global.redis.entity;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value="EmailAuth", timeToLive = 1000L * 60 * 5)
public class EmailAuth {

    @Id
    private String email;

    @Indexed
    private String authCode;

    public EmailAuth(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
    }

    public static EmailAuth of(String email, String authCode) {return new EmailAuth(email, authCode);};
}

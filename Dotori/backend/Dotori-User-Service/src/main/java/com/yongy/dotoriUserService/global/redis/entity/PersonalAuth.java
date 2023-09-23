package com.yongy.dotoriUserService.global.redis.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="PersonalAuth", timeToLive = 1000L * 60 * 3)
public class PersonalAuth {
    @Id
    private String authCode;

    private String id;

    public PersonalAuth(String authCode, String id) {
        this.authCode = authCode;
        this.id = id;
    }


    public static PersonalAuth of(String authCode, String id){
        return new PersonalAuth(authCode, id);
    }
}

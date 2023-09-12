package com.bank.podo.global.email.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "verificationCode", timeToLive = 10)
public class VerificationCode {

    @Id
    String email;

    String code;

    LocalDateTime sendAt;
}

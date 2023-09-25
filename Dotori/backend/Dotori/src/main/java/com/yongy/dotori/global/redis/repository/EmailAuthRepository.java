package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.EmailAuth;
import org.springframework.data.repository.CrudRepository;

public interface EmailAuthRepository extends CrudRepository<EmailAuth, String> {

    EmailAuth findByAuthCode(String authCode);
}

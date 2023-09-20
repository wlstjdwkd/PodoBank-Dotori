package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.SmsAuth;
import org.springframework.data.repository.CrudRepository;

public interface SmsAuthRepository extends CrudRepository<SmsAuth, String> {
}

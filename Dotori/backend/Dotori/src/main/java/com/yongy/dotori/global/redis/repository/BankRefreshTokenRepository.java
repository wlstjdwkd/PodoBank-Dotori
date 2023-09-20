package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.BankRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface BankRefreshTokenRepository extends CrudRepository<BankRefreshToken, String> {
}

package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}

package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.PodoBankRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface DotoriRefreshTokenRepository extends CrudRepository<PodoBankRefreshToken, String> {
}

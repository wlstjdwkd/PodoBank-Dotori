package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.PodoBankAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface DotoriAccessTokenRepository extends CrudRepository<PodoBankAccessToken, String> {
}

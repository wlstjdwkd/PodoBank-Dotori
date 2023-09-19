package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.DotoriToken;
import org.springframework.data.repository.CrudRepository;

public interface DotoriTokenRepository extends CrudRepository<DotoriToken, String> {
}

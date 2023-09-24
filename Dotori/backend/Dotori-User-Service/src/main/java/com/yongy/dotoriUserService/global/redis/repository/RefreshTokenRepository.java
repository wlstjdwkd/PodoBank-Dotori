package com.yongy.dotoriUserService.global.redis.repository;

import com.yongy.dotoriUserService.global.redis.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}

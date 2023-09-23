package com.yongy.dotoriUserService.global.redis.repository;

import com.yongy.dotoriUserService.global.redis.entity.EmailAuth;
import org.springframework.data.repository.CrudRepository;

public interface EmailAuthRepository extends CrudRepository<EmailAuth, String> {
}

package com.yongy.dotoriUserService.global.redis.repository;

import com.yongy.dotoriUserService.global.redis.entity.PersonalAuth;
import org.springframework.data.repository.CrudRepository;

public interface PersonalAuthRepository extends CrudRepository<PersonalAuth, String> {
}

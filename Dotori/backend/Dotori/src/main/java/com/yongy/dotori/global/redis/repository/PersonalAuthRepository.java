package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.PersonalAuth;
import org.springframework.data.repository.CrudRepository;

public interface PersonalAuthRepository extends CrudRepository<PersonalAuth, String> {

    PersonalAuth findByAuthCode(String authCode);

}

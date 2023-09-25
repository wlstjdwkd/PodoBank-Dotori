package com.yongy.dotori.global.redis.repository;

import com.yongy.dotori.global.redis.entity.BankAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface BankAccessTokenRepository extends CrudRepository<BankAccessToken, String> {
    BankAccessToken findByBankName(String bankName);

}

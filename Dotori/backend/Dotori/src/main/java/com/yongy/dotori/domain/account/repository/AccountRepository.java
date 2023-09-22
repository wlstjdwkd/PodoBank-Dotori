package com.yongy.dotori.domain.account.repository;

import com.yongy.dotori.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountSeq(Long accountSeq);
    Account findByAccountNumberAndDeleteAtIsNull(String AccountNumber);
    List<Account> findAllByUserUserSeq(Long userSeq);

}

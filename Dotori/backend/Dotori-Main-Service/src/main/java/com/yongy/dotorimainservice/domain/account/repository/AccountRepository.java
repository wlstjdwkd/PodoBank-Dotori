package com.yongy.dotorimainservice.domain.account.repository;

import com.yongy.dotorimainservice.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountSeqAndDeleteAtIsNull(Long accountSeq);
    Account findByAccountNumberAndDeleteAtIsNull(String AccountNumber);
    List<Account> findAllByUserSeqAndDeleteAtIsNull(Long userSeq);

}

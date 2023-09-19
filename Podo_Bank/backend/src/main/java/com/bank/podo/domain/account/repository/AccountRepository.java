package com.bank.podo.domain.account.repository;

import com.bank.podo.domain.account.entity.Account;
import com.bank.podo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String generatedNumber);

    List<Account> findAllByUser(User user);

    Optional<Account> findByAccountNumberAndMaturityAtIsNull(String accountNumber);

    int countByUser(User user);
}

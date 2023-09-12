package com.bank.podo.domain.account.repository;

import com.bank.podo.domain.account.entity.Account;
import com.bank.podo.domain.account.entity.TransactionHistory;
import com.bank.podo.domain.account.enums.TransactionType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findAllByAccountAndTransactionAtGreaterThanEqual(Account account, LocalDateTime transactionAt, PageRequest pageRequest);

    List<TransactionHistory> findAllByAccountAndTransactionType(Account account, TransactionType transactionType, PageRequest pageRequest);

    List<TransactionHistory> findAllByAccountAndTransactionTypeAndTransactionAtGreaterThanEqual(Account account, TransactionType transactionType, LocalDateTime transactionAt, PageRequest pageRequest);
}

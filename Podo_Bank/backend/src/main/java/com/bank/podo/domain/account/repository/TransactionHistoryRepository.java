package com.bank.podo.domain.account.repository;

import com.bank.podo.domain.account.entity.Account;
import com.bank.podo.domain.account.entity.TransactionHistory;
import com.bank.podo.domain.account.enums.TransactionType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {

    List<TransactionHistory> findAllByAccountAndCreatedAtGreaterThanEqual(Account account, LocalDateTime createdAt);
    List<TransactionHistory> findAllByAccountAndCreatedAtGreaterThanEqual(Account account, LocalDateTime createdAt, PageRequest pageRequest);

    List<TransactionHistory> findAllByAccountAndTransactionType(Account account, TransactionType transactionType, PageRequest pageRequest);

    List<TransactionHistory> findAllByAccountAndTransactionTypeAndCreatedAtGreaterThanEqual(Account account, TransactionType transactionType, LocalDateTime createdAt, PageRequest pageRequest);
}

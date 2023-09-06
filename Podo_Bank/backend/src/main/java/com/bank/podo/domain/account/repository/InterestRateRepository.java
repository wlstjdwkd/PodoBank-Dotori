package com.bank.podo.domain.account.repository;

import com.bank.podo.domain.account.entity.InterestRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRateRepository extends JpaRepository<InterestRate, Long> {
}

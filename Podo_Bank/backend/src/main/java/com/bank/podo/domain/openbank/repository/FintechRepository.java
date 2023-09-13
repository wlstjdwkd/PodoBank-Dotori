package com.bank.podo.domain.openbank.repository;

import com.bank.podo.domain.openbank.entity.FTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FintechRepository extends JpaRepository<FTUser, Long> {
}

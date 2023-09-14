package com.bank.podo.domain.openbank.repository;

import com.bank.podo.domain.openbank.entity.FTService;
import com.bank.podo.domain.openbank.entity.FTUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FintechUserRepository extends JpaRepository<FTUser, Long> {
    Optional<FTUser> findByServiceAndFintechCode(FTService ftService, String fintechCode);
}

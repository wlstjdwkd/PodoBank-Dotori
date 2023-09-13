package com.bank.podo.domain.openbank.repository;

import com.bank.podo.domain.openbank.entity.FTService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<FTService, Long> {
    Optional<FTService> findByServiceCode(String fintechServiceCode);
}

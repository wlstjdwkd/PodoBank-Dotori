package com.bank.podo.domain.fcm.repository;

import com.bank.podo.domain.fcm.entity.FCMToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FCMRepository extends JpaRepository<FCMToken, Long> {
    void deleteByEmail(String email);
}

package com.bank.podo.domain.user.repository;

import com.bank.podo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(String id);

    Optional<User> findById(String id);
}

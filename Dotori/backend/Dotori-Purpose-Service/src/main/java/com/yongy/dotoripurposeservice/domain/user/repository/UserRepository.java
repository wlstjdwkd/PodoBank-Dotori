package com.yongy.dotoripurposeservice.domain.user.repository;

import com.yongy.dotoripurposeservice.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByIdAndExpiredAtIsNull(String id);
    List<User> findAll();


}

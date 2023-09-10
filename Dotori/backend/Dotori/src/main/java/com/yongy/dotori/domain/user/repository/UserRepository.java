package com.yongy.dotori.domain.user.repository;

import com.yongy.dotori.domain.user.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository {
    User findById(String id);
}

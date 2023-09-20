package com.yongy.dotori.global.security.service;

import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public User getUserInfo(String id){
        return userRepository.findByIdAndExpiredAtIsNull(id);
    }
}

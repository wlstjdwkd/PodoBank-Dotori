package com.yongy.dotoriUserService.global.security.service;

import com.yongy.dotoriUserService.domain.user.entity.User;
import com.yongy.dotoriUserService.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public User getUserInfo(String id){
        return userRepository.findByIdAndExpiredAtIsNull(id);
    }
}

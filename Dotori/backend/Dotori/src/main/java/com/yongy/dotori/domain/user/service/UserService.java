package com.yongy.dotori.domain.user.service;

import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.global.security.jwtDto.JwtToken;

public interface UserService {
    void authEmail(String id);
    void sendAuthEmail(String id, String authKey);

    String getAuthCode(String id);

    void deleteAuthCode(String id);
}

package com.yongy.dotori.domain.user.service;

public interface UserService {
    void authEmail(String id);
    void sendAuthEmail(String id, String authKey);
    String getAuthCode(String id);
    void deleteAuthCode(String id);
}

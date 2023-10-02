package com.yongy.dotoriuserservice.domain.user.service;

import com.yongy.dotoriuserservice.domain.user.entity.User;

public interface UserService {
    public void saveUser(User user);

    public void deleteUserRefreshToken(String id);

    public void removeRetireUser(User user);

    public User getUserByUserSeq(Long userSeq);

    public User getUserById(String id);
}

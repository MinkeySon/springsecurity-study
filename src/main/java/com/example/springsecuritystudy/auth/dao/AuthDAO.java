package com.example.springsecuritystudy.auth.dao;

import com.example.springsecuritystudy.user.domain.User;

public interface AuthDAO {
    void createUser(User user);
}

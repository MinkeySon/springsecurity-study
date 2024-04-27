package com.example.springsecuritystudy.user.dao;

import com.example.springsecuritystudy.user.domain.User;

public interface UserDAO {
    User findUser(String email);
}

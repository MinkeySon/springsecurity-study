package com.example.springsecuritystudy.user.dao.impl;

import com.example.springsecuritystudy.user.dao.UserDAO;
import com.example.springsecuritystudy.user.domain.User;
import com.example.springsecuritystudy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@RequiredArgsConstructor
@Component
public class UserDAOImpl implements UserDAO {
    private final UserRepository userRepository;
    @Override
    public User findUser(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }
}

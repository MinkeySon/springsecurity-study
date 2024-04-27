package com.example.springsecuritystudy.auth.dao.impl;

import com.example.springsecuritystudy.auth.dao.AuthDAO;
import com.example.springsecuritystudy.user.domain.User;
import com.example.springsecuritystudy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthDAOImpl implements AuthDAO {
    private final UserRepository userRepository;
    @Override
    public void createUser(User user) {

        userRepository.save(user);
    }
}

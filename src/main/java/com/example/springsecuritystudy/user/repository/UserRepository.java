package com.example.springsecuritystudy.user.repository;

import com.example.springsecuritystudy.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickName(String nickName);
}

package com.example.springsecuritystudy.auth.service;

import com.example.springsecuritystudy.auth.dto.request.SignInRequestDto;
import com.example.springsecuritystudy.auth.dto.request.SignUpRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> signUp(SignUpRequestDto signUpRequestDto);
    ResponseEntity<?> signIn(SignInRequestDto signInRequestDto);
}

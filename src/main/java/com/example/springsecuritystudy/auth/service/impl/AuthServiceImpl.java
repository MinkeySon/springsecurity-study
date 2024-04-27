package com.example.springsecuritystudy.auth.service.impl;

import com.example.springsecuritystudy.auth.dao.AuthDAO;
import com.example.springsecuritystudy.auth.dto.request.SignInRequestDto;
import com.example.springsecuritystudy.auth.dto.request.SignUpRequestDto;
import com.example.springsecuritystudy.auth.dto.response.AuthResponseDto;
import com.example.springsecuritystudy.auth.service.AuthService;
import com.example.springsecuritystudy.global.domain.CommonResponse;
import com.example.springsecuritystudy.global.domain.enums.ResultCode;
import com.example.springsecuritystudy.global.security.JwtTokenProvider;
import com.example.springsecuritystudy.user.dao.UserDAO;
import com.example.springsecuritystudy.user.domain.User;
import com.example.springsecuritystudy.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    @Override
    public ResponseEntity<?> signUp(SignUpRequestDto signUpRequestDto) {
        if(checkEmailPresence(signUpRequestDto.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    AuthResponseDto.builder()
                            .status(CommonResponse.fail(ResultCode.DUPLICATION_EMAIL))
                            .build()
            );
        }
        if(checkNickNamePresence(signUpRequestDto.getNickName())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    AuthResponseDto.builder()
                            .status(CommonResponse.fail(ResultCode.DUPLICATION_NICKNAME))
                            .build()
            );
        }
        User user = User.builder()
                .userName(signUpRequestDto.getUserName())
                .nickName(signUpRequestDto.getNickName())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .phoneNumber(signUpRequestDto.getPhoneNumber())
                .email(signUpRequestDto.getEmail())
                .profileUrl(signUpRequestDto.getProfileUrl())
                .loginType(signUpRequestDto.getLoginType())
                .useAble(signUpRequestDto.getUseAble())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        authDAO.createUser(user);

        return ResponseEntity.status(HttpStatus.OK).body(CommonResponse.success());
    }

    @Override
    public ResponseEntity<?> signIn(SignInRequestDto signInRequestDto) {
        if(!checkEmailPresence(signInRequestDto.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    CommonResponse.fail(ResultCode.NOT_FOUND_USER)
            );
        }

        User user = userDAO.findUser(signInRequestDto.getEmail());

        if(!passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    CommonResponse.fail(ResultCode.PASSWORD_NOT_MATCH)
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(AuthResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(user.getEmail(), user.getRoles()))
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getEmail()))
                .status(CommonResponse.success())
                .build());
    }
    private boolean checkEmailPresence(String email){
        return userRepository.existsByEmail(email);
    }
    private boolean checkNickNamePresence(String nickName){
        return userRepository.existsByNickName(nickName);
    }
}

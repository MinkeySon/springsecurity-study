package com.example.springsecuritystudy.auth.dto.response;

import com.example.springsecuritystudy.global.domain.CommonResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthResponseDto {
    private String accessToken;
    private String refreshToken;
    private CommonResponse<?> status;
}

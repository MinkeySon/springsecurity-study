package com.example.springsecuritystudy.global.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String key;

    private final long accessTokenValidTime = 1000L * 60 * 60;
    private final long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 14;

    public String createAccessToken(String email, List<String> roles){
        log.info("[JwtTokenProvider] create access token");
        String token =  createToken(email, roles, accessTokenValidTime);
        log.info("[JwtTokenProvider] create access token success");
        return token;
    }

    public String createRefreshToken(String email){
        log.info("[JwtTokenProvider] create refresh token");
        String token =  createToken(email, new ArrayList<>(), refreshTokenValidTime);
        log.info("[JwtTokenProvider] create refresh token success");
        return token;
    }

    private String createToken(String email, List<String> roles, long validTime){
        log.info("[JwtTokenProvider] create token");
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", roles);

        Date now = new Date();

        byte[] keyBytes = key.getBytes();
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+validTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("[JwtTokenProvider] create token success");

        return token;
    }

    public Authentication getAuthentication(String token){
        log.info("[JwtTokenProvider] check authentication");
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserEmail(token));
        log.info("[JwtTokenProvider] check authentication success");
        return new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());
    }

    public String getUserEmail(String token){
        log.info("[JwtTokenProvider] check user email");
        String info = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        log.info("[JwtTokenProvider] check user email success");
        return info;
    }

    public String refreshToken(String refreshToken, String email){
        log.info("[JwtTokenProvider] refresh access token");

        if (validateToken(refreshToken)){
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            List<String> roles = userDetails.getAuthorities().stream().map(authority -> authority.getAuthority())
                    .collect(Collectors.toList());

            return createAccessToken(email, roles);
        }else{
            throw new RuntimeException("refresh token is not valid");
        }
    }

    public String extractToken(HttpServletRequest request){
        log.info("[JwtTokenProvider] extract token from header");
        return request.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateToken(String token){
        log.warn("[JwtTokenProvider] validate token");

        try{
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            log.info("[JwtTokenProvider] token is invalid");
            return false;
        }
    }

}

package com.bank.podo.global.security.service;

import com.bank.podo.domain.user.exception.UserNotFoundException;
import com.bank.podo.domain.user.repository.UserRepository;
import com.bank.podo.domain.user.enums.Role;
import com.bank.podo.domain.user.entity.User;
import com.bank.podo.global.security.entity.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private static final String SECRET_KEY = "secret-key";
    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60; // 1시간
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 7 * 24 * 60 * 60; // 7일
    private final UserRepository userRepository;

    public Token generateToken(String id, Role role) {
        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(id)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(id)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_SECONDS * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();

        return new Token(accessToken, refreshToken);
    }

    public boolean verifyToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // 토큰 만료시간 확인
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date()); // 만료 시간이 지났으면(true) -> false 반환
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public User getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        String id = claims.getSubject();
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디입니다."));
    }

    public boolean verifyRefreshTokenOwner(String token, User user) {
        return false;
    }

    public void saveRefreshToken(String email, String refreshToken) {

    }

    public Token generateAccessTokenByRefreshToken(String refreshToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // 토큰 만료시간 확인
            Date expiration = claims.getExpiration();
            if(!expiration.before(new Date())) {
                return null;
            } else {
                return generateToken(claims.getSubject(), (Role) claims.get("role"));
            }
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}


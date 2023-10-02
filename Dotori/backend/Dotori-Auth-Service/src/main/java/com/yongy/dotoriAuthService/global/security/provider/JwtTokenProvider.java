package com.yongy.dotoriAuthService.global.security.provider;

import com.yongy.dotoriAuthService.domain.user.entity.Role;
import com.yongy.dotoriAuthService.global.security.dto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    private final long exp = 1000L * 60 * 60; // 1시간

    private Key secretKey;

    @PostConstruct
    protected void init(){
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    public JwtToken createToken(String id, Role role){
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role", role.toString()); // ADMIN 또는 USER
        Date now = new Date();

        // 만료시간 : 1시간
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp)) // 1시간
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // 만료시간 : 24시간
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (exp * 24 * 7))) // 7일
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    public Claims getClaims(String token){
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        try{
            claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e) {
            log.info("만료된 토큰");
            throw new BadCredentialsException("만료된 토큰", e);
        } catch (MalformedJwtException e) {
            log.info("유효하지 않은 구성의 토큰");
            throw new BadCredentialsException("유효하지 않은 구성의 토큰", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 형식이나 구성의 토큰");
            throw new BadCredentialsException("지원되지 않는 형식이나 구성의 토큰", e);
        } catch (IllegalArgumentException e) {
            log.info("잘못된 입력값");
            throw new BadCredentialsException("잘못된 입력값", e);
        }
        return claims;
    }

    public String getUserIdFromToken(String token){
        return this.getClaims(token).getSubject();
    }


}
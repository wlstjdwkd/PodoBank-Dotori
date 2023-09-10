package com.bank.podo.global.security.filter;

import com.bank.podo.global.security.service.JwtProvider;
import com.bank.podo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    // 헤더 내부에서 JWT 용으로 사용 할 Key
    // 보통 Authorization
    public static final String HEADER_KEY = "Authorization";

    // 인증 타입
    // JWT는 Bearer 토큰의 일종
    public static final String PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Request received: " + request.getRemoteAddr() + " " + request.getMethod() + " " + request.getRequestURI());
        // 헤더에서 토큰 분리
        String token = extractTokenFromRequest(request);

        // 토큰 유효성 검증
        if (StringUtils.hasText(token)) {
            // 토큰이 있는 경우
            if (jwtProvider.verifyToken(token)) { // 권한 확인
                Authentication authentication = jwtProvider.getAuthentication(token); // 인증 정보와 권한 가져오기
                SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder : spring security 인메모리 세션저장소
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_KEY);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(PREFIX)) {
            return bearerToken.substring(PREFIX.length());
        }
        return null;
    }

}

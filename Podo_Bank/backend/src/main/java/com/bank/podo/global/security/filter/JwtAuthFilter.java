package com.bank.podo.global.security.filter;

import com.bank.podo.global.security.service.TokenService;
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

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Request received: " + request.getRemoteAddr() + " " + request.getMethod() + " " + request.getRequestURI());
        String token = extractTokenFromRequest(request);
        if (isPermitAllRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 접근권한(로그인)이 필요한 요청이면
        if (StringUtils.hasText(token)) {
            // 토큰이 있는 경우
            if (tokenService.verifyToken(token)) { // 권한 확인
                User user = tokenService.getUserFromToken(token); // 토큰에서 유저정보 가져오기
                Authentication authentication = getAuthentication(user); // 인증 정보와 권한 가져오기
                SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContextHolder : spring security 인메모리 세션저장소
                log.info("====================");
                log.info("AUTHENTICATION SUCCESS");
                log.info("USERNAME : " + ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
                log.info("ROLE : " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
                log.info("====================");
                filterChain.doFilter(request, response);
                return;
            } else { // 만료 시간이 지났으면
                log.info("====================");
                log.info("ACCESSTOKEN EXPIRED");
                log.info("====================");

                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Has Expired");   // 401
                return;
            }
        }
        log.info("====================");
        log.info("ACCESSTOKEN NOT EXIST");
        log.info("====================");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Not Exist");
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, null, Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));
    }

    private boolean isPermitAllRequest(HttpServletRequest request) {
        List<RequestMatcher> matchers = new ArrayList<>();

        // HttpSecurity 클래스에서 permitAll()로 허용한 URL 패턴 가져오기
        matchers.add(new AntPathRequestMatcher("/favicon.ico"));
        matchers.add(new AntPathRequestMatcher("/"));

        matchers.add(new AntPathRequestMatcher("/api/v1/user/register"));
        matchers.add(new AntPathRequestMatcher("/api/v1/user/login"));

        matchers.add(new AntPathRequestMatcher("/v2/api-docs/**"));
        matchers.add(new AntPathRequestMatcher("/swagger-ui/**"));
        matchers.add(new AntPathRequestMatcher("/swagger-resources/**"));

        matchers.add(new AntPathRequestMatcher("/actuator/**"));

        // 요청 URL이 permitAll()로 허용한 URL 패턴에 해당하는지 확인
        for (RequestMatcher matcher : matchers) {
            if (matcher.matches(request)) {
                return true;
            }
        }
        return false;
    }
}

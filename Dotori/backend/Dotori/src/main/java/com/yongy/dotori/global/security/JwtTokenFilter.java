package com.yongy.dotori.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class JwtTokenFilter extends GenericFilterBean {
    private JwtTokenProvider provider;

    public JwtTokenFilter(JwtTokenProvider provider){
        this.provider = provider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException{
            String token = ((HttpServletRequest) request).getHeader(JwtTokenProvider.headerKey);

            // 유효한 토큰인지 확인한다.
            if(token != null && provider.tokenValidCheck(token)){
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아온다.
                Authentication authentication = provider.getTokenInfo(token);
                System.out.println(authentication.getName()+"/"+authentication.getAuthorities().size());
                // SecurityContext에 Authentication 객체를 저장한다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println(authentication);
            }
            chain.doFilter(request, response); // 현재 필터에서의 처리가 끝나고 다음 필터로 제어를 넘기는 역할을 한다.
    }
}

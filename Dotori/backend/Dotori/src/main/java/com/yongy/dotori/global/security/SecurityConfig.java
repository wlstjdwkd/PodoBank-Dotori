package com.yongy.dotori.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    private final SecurityUserService service;
    private final JwtTokenFilter filter;
    private final JwtTokenProvider provide;

    // 비밀번호를 DB에 저장하기 전 사용할 암호화
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public SecurityConfig(SecurityUserService service, JwtTokenFilter filter, JwtTokenProvider provide){
        this.service = service;
        this.filter = filter;
        this.provide = provide;
    }


    // css, js, 일반 접근(/)에 대해서는 시큐리티가 검사하지 않고 허용하겠다는 의미이다.
    @Bean
    WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().anyRequest().requestMatchers("/css/**","/js/**","/img/**","/");
    }


}

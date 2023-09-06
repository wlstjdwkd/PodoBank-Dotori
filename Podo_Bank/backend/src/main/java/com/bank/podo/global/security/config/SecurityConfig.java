package com.bank.podo.global.security.config;

import com.bank.podo.global.security.filter.JwtAuthFilter;
import com.bank.podo.global.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@EnableWebSecurity  // 필터를 스프링 필터 체인에 등록
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenService tokenService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // jwt 로그인
        http.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .authorizeHttpRequests()
                .antMatchers("/favicon.ico").permitAll()

                .antMatchers("/api/v1/user/register", "/api/v1/user/login").permitAll()

                .antMatchers("/v2/api-docs/**", "/swagger-ui/**", "/actuator/**", "/swagger-resources/**").permitAll() // Swagger 접속 주소를 허용
                .antMatchers("/api/v1/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new JwtAuthFilter(tokenService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout().disable();
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // 모든 요청을 허용
        configuration.addAllowedMethod("*"); // 모든 메소드를 허용
        configuration.addAllowedHeader("*"); // 모든 헤더를 허용
        configuration.setAllowCredentials(true); // 쿠키 인증 허용
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

package com.yongy.dotorimainservice.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotorimainservice.domain.user.entity.User;
import com.yongy.dotorimainservice.global.security.dto.response.MegResDto;
import com.yongy.dotorimainservice.global.security.exception.ErrorType;
import com.yongy.dotorimainservice.global.security.provider.AuthProvider;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class AuthFilter extends GenericFilterBean {

    private final AuthProvider authProvider;

    @PostConstruct
    public void init() {
        log.info("----------------------------INIT_FILTER----------------------------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("----------------------------DO_FILTER----------------------------");

        HttpServletRequest httpServletRequest = (HttpServletRequest)request;

        String id = httpServletRequest.getHeader("id");

        log.info("id : "+ id);

        Authentication authentication = null;
        User user = authProvider.getUserFromHeaderId(id);
        if(user != null){
            authentication = authProvider.getAuthentication(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }else{ // NOTE : 사용자가 존재하지 않습니다.
//            generalJwtExceptionHandler((HttpServletResponse) response, ErrorType.NOT_FOUND_USER);
//            return;
        }
        chain.doFilter(request, response);
    }

    public void generalJwtExceptionHandler(HttpServletResponse response, ErrorType error) {
        response.setStatus(error.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        MegResDto dto = new MegResDto(error.getCode(), error.getMessage());

        try {
            String json = new ObjectMapper().writeValueAsString(dto);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}

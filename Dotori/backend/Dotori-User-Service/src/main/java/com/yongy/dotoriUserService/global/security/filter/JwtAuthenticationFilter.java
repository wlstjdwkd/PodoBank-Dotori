package com.yongy.dotoriUserService.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotoriUserService.domain.user.entity.User;
import com.yongy.dotoriUserService.global.security.dto.response.MegResDto;
import com.yongy.dotoriUserService.global.security.exception.ErrorType;
import com.yongy.dotoriUserService.global.security.provider.JwtTokenProvider;
import com.yongy.dotoriUserService.global.security.service.UserDetailsService;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsService userDetailsService;

    private final long exp = 1000L * 60 * 60;

    @PostConstruct
    public void init() {
        log.info("----------------------------INIT_FILTER----------------------------");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("----------------------------DO_FILTER----------------------------");

        String token = resolveToken((HttpServletRequest) request); // token : Bearer [토큰]

        if(token != null){
            token = token.split(" ")[1].trim();

            // NOTE : 우리가 만든 토큰이 아닌 경우
            String tokenId = jwtTokenProvider.getUserId(token);
            try{
                tokenId = jwtTokenProvider.getUserId(token);
            }catch(Exception e){
                generalJwtExceptionHandler((HttpServletResponse) response, ErrorType.NOT_OURS_TOKEN);
                return;
            }

            // NOTE : DB에 사용자가 없는 경우 or 정보는 있지만 탈퇴한 경우
            User user = userDetailsService.getUserInfo(tokenId);
            if(user == null){
                generalJwtExceptionHandler((HttpServletResponse)response, ErrorType.NOT_FOUND_USER);
                return;
            }

            if(jwtTokenProvider.validateToken(resolveToken((HttpServletRequest) request))){
                // NOTE : DB에 사용자의 정보가 있는 경우 (인증객체 생성)
                Authentication auth = jwtTokenProvider.getAuthentication(user, tokenId);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else {
                // NOTE : refreshToken을 다시 발급받으세요
                generalJwtExceptionHandler((HttpServletResponse) response, ErrorType.EXPIRATION_ACCESS_TOKEN);
                return;
            }
        }else{
            // NOTE : accessToken이 존재하지 않음 => Controller에서 accessToken, refreshToken 발급받기
        }
        chain.doFilter(request, response);
    }

    // NOTE : 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken;
        }
        return null;
    }

    // NOTE : 토큰에 대한 오류가 발생했을 때, 커스터마이징해서 Exception 처리 값을 클라이언트에게 알려준다.
    // code, message를 담아서 반환한다.
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

    // code, message, accessToken을 담아서 반환한다.
//    public void newJwtExceptionHandler(HttpServletResponse response, ErrorType error, String accessToken) {
//        response.setStatus(error.getCode());
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        TokenMsgResDto dto = new TokenMsgResDto(error.getCode(), error.getMessage(), accessToken);
//
//        try {
//            String json = new ObjectMapper().writeValueAsString(dto);
//            response.getWriter().write(json);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }


}

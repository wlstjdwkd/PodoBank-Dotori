package com.yongy.dotori.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yongy.dotori.domain.user.entity.Role;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.global.redis.RedisUtil;
import com.yongy.dotori.global.security.jwtDto.JwtToken;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;


@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    private RedisUtil redisUtil;

    private UserDetailsService userDetailsService;

    private final long exp = 1000L * 60 * 60;

//    @PostConstruct
//    public void init(){
//        redisUtil = new RedisUtil();
//        userDetailsService = new UserDetailsService();
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("----------------------------FILTER----------------------------");

        redisUtil = new RedisUtil();
        userDetailsService = new UserDetailsService();

        String token = resolveToken((HttpServletRequest) request); // token : Bearer [토큰]

        if(token != null){
            log.info("TOKEN : "+ token);
            // TODO : accessToken이 유효함
            token = token.split(" ")[1].trim();

            String tokenId = null;
            try{
                tokenId = jwtTokenProvider.getUserId(token);
            }catch(Exception e){
                generalJwtExceptionHandler((HttpServletResponse) response, ErrorType.NOT_OURS_TOKEN);
            }

            User user = userDetailsService.getUserInfo(tokenId);

            if(jwtTokenProvider.validateToken(token)){ // accessToken이 유효함
                // TODO : DB에 사용자가 없는 경우 or 정보는 있지만 탈퇴한 경우
                if(user == null || user.getExpiredAt() != null){
                    generalJwtExceptionHandler((HttpServletResponse)response, ErrorType.NOT_FOUND_USER);
                }

                // TODO : DB에 사용자의 정보가 있는 경우 -- 인증객체생성
                log.info("DOTORI-VALID-CHECK");

                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }else { // accessToken이 유효하지 않음

                // TODO : DB에 사용자가 없는 경우 or 정보는 있지만 탈퇴한 경우
                if (user == null || user.getExpiredAt() != null) {
                    generalJwtExceptionHandler((HttpServletResponse) response, ErrorType.NOT_FOUND_USER);
                }

                // TODO : refreshToken의 여부에 관계없이 재발급
                String refreshToken = redisUtil.getData(tokenId);

                // refreshToken이 있던 없던 재발급
                JwtToken jwtToken = jwtTokenProvider.createToken(tokenId, Role.USER);

                // refreshToken 저장하기
                redisUtil.setDataExpire(tokenId, jwtToken.getRefreshToken(), exp * 24);

                // accessToken을 client에게 반환하기
                newJwtExceptionHandler((HttpServletResponse) response, ErrorType.NEW_ACCESS_TOKEN, jwtToken.getAccessToken());
            }

        }else{
            // TODO : accessToken이 존재하지 않음 => Controller에서 accessToken, refreshToken 발급받기
        }

        chain.doFilter(request, response);
    }

    // TODO : 헤더에서 토큰 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken;
        }
        return null;
    }

    // TODO : 토큰에 대한 오류가 발생했을 때, 커스터마이징해서 Exception 처리 값을 클라이언트에게 알려준다.
    // code, message를 담아서 반환한다.
    public void generalJwtExceptionHandler(HttpServletResponse response, ErrorType error) {
        response.setStatus(error.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        MessageResponseDto dto = new MessageResponseDto(error.getCode(), error.getMessage());

        try {
            String json = new ObjectMapper().writeValueAsString(dto);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    // code, message, accessToken을 담아서 반환한다.
    public void newJwtExceptionHandler(HttpServletResponse response, ErrorType error, String accessToken) {
        response.setStatus(error.getCode());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        TokenMessageResponseDto dto = new TokenMessageResponseDto(error.getCode(), error.getMessage(), accessToken);

        try {
            String json = new ObjectMapper().writeValueAsString(dto);
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}

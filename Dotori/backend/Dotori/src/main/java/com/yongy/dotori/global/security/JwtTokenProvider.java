package com.yongy.dotori.global.security;
// TODO: 토큰을 만들고 분석하는 클래스

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {
    public static String headerKey = "Authorization";
    private String secretKey = "jyjofSecretKey";
    private long time = 60 * 60 * 1000L; // 토큰의 유효시간 : 60분

    private final SecurityUserService securityUserService;

    public JwtTokenProvider(SecurityUserService securityUserService){
        this.securityUserService = securityUserService;
    }

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    // @PostConstruct : 클래스의 초기화 메서드를 지정한다. 해당 클래스의 객체가 생성되고 모든 의존성 주입이 완료된 후 자동으로 호출된다.
    @PostConstruct
    protected void secretKeyEncoding(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰을 생성한다.
    public String createToken(String id){
        Claims claims = Jwts.claims().setSubject(id); // JWT payload에 저장되는 정보의 단위, id를 넣음(사용자를 식별하는 값)
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + time))
                .signWith(SignatureAlgorithm.ES256, secretKey)
                .compact();
    }

    // JWT 토큰에서 인증 정보를 조회한다.
    public Authentication getTokenInfo(String token){
        UserDetails userDetails = securityUserService.loadUserByUsername(this.getUserInfo(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean tokenValidCheck(String token){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date()); // 부정 연산자로 토큰의 만료 시간이 현재 시간 이전이면 true, 만료되지 않았으면 false를 반환한다.
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // 토큰에서 회원의 정보를 추출한다.
    private String getUserInfo(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }


}

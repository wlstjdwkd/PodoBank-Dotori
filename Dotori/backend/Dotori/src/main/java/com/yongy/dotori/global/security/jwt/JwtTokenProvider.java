package com.yongy.dotori.global.security.jwt;

import com.yongy.dotori.domain.user.entity.Role;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.user.service.UserService;
import com.yongy.dotori.global.security.jwtDto.JwtToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String salt;

    private final long exp = 1000L * 60 * 60;

    private Key secretKey;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    protected void init(){
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    public JwtToken createToken(String id, Role role){
        Claims claims = Jwts.claims().setSubject(id);
        claims.put("role", role.toString()); // ADMIN 또는 USER
        Date now = new Date();

        // 만료시간 : 1시간
        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        // 만료시간 : 24시간
        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (exp * 24)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

    }

    // 권한정보 획득
    // Spring Security 인증과정에서 권한획득을 위한 기능
    public Authentication getAuthentication(String token){
        String id = this.getUserId(token);
        User user = userRepository.findById(id);
        System.out.println("ID : " + user.getId());
        return new UsernamePasswordAuthenticationToken(user, "", Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())));
    }

    public String getUserId(String token){
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getSubject();
    }


    // 토큰 검증
    public boolean validateToken(String token){
        try{
            // Bearer 검증
            if(!token.substring(0, "Bearer ".length()).equalsIgnoreCase("Bearer ")){
                return false;
            }else{
                token = token.split(" ")[1].trim();
            }
            // setSigningKey(secretKey) : secretKey가 토큰을 생성할 때 사용된 비밀 키와 일치해야함
            // parseClaimsJws(token) : 실제로 JWT 토큰을 파싱하고 클레임을 추출하는 부분이다.
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        }catch(Exception e){
            return false;
        }
    }

    // SecurityContextHolder에 저장되어 있는 User 객체 가져오기
    public User getAuthUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        User user = null;
        if(principal instanceof User){
            user = (User) principal;
            System.out.println(user.getId());
        }
        return user;
    }

//
//    public JwtToken generateToken(String id, Role role) {
////        String authorities = authentication.getAuthorities().stream()
////                .map(GrantedAuthority::getAuthority)
////                .collect(Collectors.joining(","));
//
//        //Access Token 생성
//        String accessToken = Jwts.builder()
//                .setSubject(id)
//                .claim("role", role)
//                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 30))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        //Refresh Token 생성
//        String refreshToken = Jwts.builder()
//                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 60 * 36))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//
//        return JwtToken.builder()
//                .grantType("Bearer")
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
//    }
//
//    public Authentication getAuthentication(String accessToken) {
//        //토큰 복호화
//        Claims claims = parseClaims(accessToken);
//
//        if (claims.get("auth") == null) {
//            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
//        }
//
//        Collection<? extends GrantedAuthority> authorities =
//                Arrays.stream(claims.get("auth").toString().split(","))
//                        .map(SimpleGrantedAuthority::new)
//                        .collect(Collectors.toList());
//
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
//            return true;
//        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
//            log.info("Invalid JWT Token", e);
//        } catch (ExpiredJwtException e) {
//            log.info("Expired JWT Token", e);
//        } catch (UnsupportedJwtException e) {
//            log.info("Unsupported JWT Token", e);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty.", e);
//        }
//        return false;
//    }
//
//    private Claims parseClaims(String accessToken) {
//        try {
//            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }


}
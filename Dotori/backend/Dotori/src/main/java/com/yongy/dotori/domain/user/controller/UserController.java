package com.yongy.dotori.domain.user.controller;

import com.yongy.dotori.domain.user.dto.request.UserInfoDto;
import com.yongy.dotori.domain.user.dto.request.UserLoginDto;
import com.yongy.dotori.domain.user.entity.Provider;
import com.yongy.dotori.domain.user.entity.Role;
import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.exception.ExceptionEnum;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.domain.user.service.UserService;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.redis.RedisUtil;
import com.yongy.dotori.global.security.exception.ErrorType;
import com.yongy.dotori.global.security.provider.JwtTokenProvider;
import com.yongy.dotori.global.security.dto.JwtToken;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtil redisUtil;

    private final long exp = 1000L * 60 * 60;

    @PostMapping("/check-id")
    public ResponseEntity<? extends BaseResponseBody> validIdCheck(@RequestParam(name="id") String id){
        User user = userRepository.findById(id);
        if(user == null){
            // 이메일 인증을 한다.
            userService.authEmail(id);
            log.info("come!");
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "이메일 인증을 끝냈습니다."));
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4001, ExceptionEnum.ALREADY_EXIST_ID));
        }
    }

    @PostMapping("/check-code")
    public ResponseEntity<? extends BaseResponseBody> validCodeCheck(
            @RequestParam(name="id") String id,
            @RequestParam(name="code") String code){
        String authCode = userService.getAuthCode(id); // 인증번호 검증
        if(authCode == null) { // 인증번호의 시간이 만료됨
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4002, ExceptionEnum.EXPIRED_AUTHCODE));
        }else if(authCode.equals(code)){
            userService.deleteAuthCode(id); // 인증번호 삭제
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "유효한 코드입니다."));
        }else{ // 인증번호가 틀림
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4003, ExceptionEnum.INVALID_AUTHCODE));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<? extends BaseResponseBody> signup(@RequestBody UserInfoDto userInfoDto){
        try{
            User user = User.builder()
                    .id(userInfoDto.getId())
                    .password(passwordEncoder.encode(userInfoDto.getPassword())) // 사용자의 비밀번호를 암호화하기
                    .userName(userInfoDto.getUserName())
                    .birthDate(LocalDate.parse(userInfoDto.getBirthDate()))
                    .phoneNumber(userInfoDto.getPhoneNumber())
                    .authProvider(Provider.DOTORI)
                    .build();

            // 비밀번호 암호화해서 저장하기
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, "회원가입을 완료했습니다."));
        }catch(Exception e){
            log.info("회원가입 오류");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponseBody.of(4004, "회원가입 오류"));
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<? extends BaseResponseBody> dotoriLogin(@RequestBody UserLoginDto userLoginDto) {

        User user = userRepository.findById(userLoginDto.getId());

        if(user == null || user.getExpiredAt() != null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "아이디를 확인해주세요."));
        }

        if(user.getAuthProvider().equals(Provider.DOTORI)){
            if(!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseBody.of(404, "비밀번호를 확인해주세요."));
            }
        }

        JwtToken jwtToken = jwtTokenProvider.createToken(user.getId(), Role.ROLE_USER);

        // refreshToken 저장
        redisUtil.setDataExpire(user.getId(), jwtToken.getRefreshToken(), exp*24);

        // accessToken 전달
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, jwtToken));
    }


    // NOTE : RefreshToken이 유효하면 accessToken, refreshToken을 재발급 받아서 반환
    @PostMapping("/new-token")
    public ResponseEntity<? extends BaseResponseBody> generateNewToken(String refreshToken){


        String id = jwtTokenProvider.getUserId(refreshToken);

        String db_refreshToken = redisUtil.getData(id);

        // refreshToken이 유효하면
        if(db_refreshToken != null){
            JwtToken jwtToken = jwtTokenProvider.createToken(id, Role.ROLE_USER);
            // refreshToken 저장하기
            redisUtil.setDataExpire(id, jwtToken.getRefreshToken(), exp * 24);

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(200, jwtToken));
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseBody.of(404, "다시 로그인하세요"));
    }
}

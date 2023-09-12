package com.yongy.dotori.domain.user.controller;

import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import com.yongy.dotori.global.common.BaseResponseBody;
import com.yongy.dotori.global.security.jwt.JwtTokenProvider;
import com.yongy.dotori.global.security.jwtDto.JwtToken;

//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider provider;

//    @PostMapping("/id/check")
//    public ResponseEntity<? extends BaseResponseBody> validIdCheck(){
//        return ResponseEntity.status(HttpStatus.O
//    }


    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Map<String, String> loginForm){
        String id = loginForm.get("id");

        User user = userRepository.findById(id);

        if(user == null){
            // 유효한 토큰이지만 DB에 데이터가 없는 경우
            return null;
        }else{
//            User authUser = provider.getAuthUser();
//
//            if(authUser != null && !authUser.getId().equals(id)){
//                JwtToken newToken = provider.createToken(user.getId(), user.getRole());
//                return new ResponseEntity<>(newToken, HttpStatus.OK);
//            }
            JwtToken newToken = provider.createToken(user.getId(), user.getRole());

            return new ResponseEntity<>(newToken, HttpStatus.OK);

           // return new ResponseEntity<>("이미 인증이 완료된 토큰입니다.", HttpStatus.OK);
        }


    }

}

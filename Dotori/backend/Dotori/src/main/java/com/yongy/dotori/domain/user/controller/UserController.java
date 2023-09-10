package com.yongy.dotori.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserController {
    // 이메일이 DB에 있는지 확인한다.
    // 회원가입



//    @Operation(summary = "회원 탈퇴 요청", description = "회원 정보가 삭제됩니다.", tags = { "Member Controller" })
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
//            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
//            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
//    })
//    @GetMapping
//    public List<String> list() {
//        return Arrays.asList("Junho Park, Jinyong Park");
//    }
}

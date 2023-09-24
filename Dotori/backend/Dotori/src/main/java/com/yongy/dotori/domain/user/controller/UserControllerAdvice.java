package com.yongy.dotori.domain.user.controller;

import com.yongy.dotori.domain.user.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 400 Bad Request : 잘못된 문법으로 인하여 서버가 요청을 이해할 수 없음
// 401 Unauthorized : 클라이언트는 요청한 응답을 받기 위해 스스로 인증해야 한다.(비승인)
// 403 Forbidden : 클라이언트가 접근할 권리를 가지고 있지 않다.(미승인) => 401과 다른 점은 서버가 클라이언트가 누군지 알고 있다.
// 404 Not Found : 서버는 요청받은 리소스를 찾을 수 없다.(API에서 종점은 적절하지만 리소스 자체가 존재하지 않음을 의미한다)
// 409 Conflict : 요청이 현재 서버의 상태와 충돌될 때 보낸다.

@ControllerAdvice
public class UserControllerAdvice {


    // NOTE : 이미 존재하는 사용자입니다.
    @ExceptionHandler
    public ResponseEntity<?> handleAlreadyExistIdException(AlreadyExistIdException e){
        return ResponseEntity.status(409).body(e.getMessage());
    }

    // NOTE : 인증번호가 만료되었습니다, 다시 로그인 해주세요.
    @ExceptionHandler
    public ResponseEntity<?> handleExpiredAuthCodeException(ExpiredAuthCodeException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    // NOTE : 인증번호가 올바르지 않습니다.
    @ExceptionHandler
    public ResponseEntity<?> handleInvalidAuthCodeException(InvalidAuthCodeException e){
        return ResponseEntity.status(409).body(e.getMessage());
    }

    // NOTE : 회원가입에 실패했습니다.
    @ExceptionHandler
    public ResponseEntity<?> handleFailedSignupException(FailedSignupException e){
        return ResponseEntity.status(400).body(e.getMessage());
    }

    // NOTE : 아이디를 확인해주세요
    @ExceptionHandler
    public ResponseEntity<?> handleInvalidIdException(InvalidIdException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    // NOTE : 비밀번호를 확인해주세요
    @ExceptionHandler
    public ResponseEntity<?> handleInvalidPwdException(InvalidPwdException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }

    // NOTE : 네이버, 카카오 로그인은 비밀번호를 변경할 수 없습니다.
    @ExceptionHandler
    public ResponseEntity<?> handleAccessDeniedSocialPwdException(AccessDeniedSocialPwdException e){
        return ResponseEntity.status(403).body(e.getMessage());
    }

    // NOTE : 네이버 인증에 실패했습니다, 카카오 인증에 실패했습니다.
    @ExceptionHandler
    public ResponseEntity<?> handleFailedSocialAuthException(FailedSocialAuthException e){
        return ResponseEntity.status(403).body(e.getMessage());
    }

    // NOTE : 탈퇴할 수 없습니다.
    @ExceptionHandler
    public ResponseEntity<?> handleFailedRetiredException(FailedRetiredException e){
        return ResponseEntity.status(404).body(e.getMessage());
    }


}

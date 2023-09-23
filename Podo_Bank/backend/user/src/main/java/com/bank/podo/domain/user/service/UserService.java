package com.bank.podo.domain.user.service;

import com.bank.podo.domain.user.dto.*;
import com.bank.podo.domain.user.entity.User;
import com.bank.podo.domain.user.exception.AlreadyUsedUsernameException;
import com.bank.podo.domain.user.exception.FromatException;
import com.bank.podo.domain.user.exception.PasswordNotMatchException;
import com.bank.podo.domain.user.repository.UserRepository;
import com.bank.podo.global.request.RequestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RequestUtil requestUtil;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public void checkUsername(String email) {
        checkEmailFormat(email);
        if(userRepository.existsByEmail(email)) {
            throw new AlreadyUsedUsernameException("이미 사용중인 아이디입니다.");
        }
    }

    public boolean logout(String email) {
        boolean isLogout = requestUtil.removeRefreshToken(email);

        logLogout(email, isLogout);

        return isLogout;
    }

    @Transactional(readOnly = true)
    public UserInfoDTO getUserInfo(String email) {
        User user = getLoginUser(email);
        return toUserInfoDTO(user);
    }

    @Transactional
    public void changePassword(String email, ChangePasswordDTO changePasswordDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser(email);

        if(!passwordEncoder.matches(changePasswordDTO.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("기존 비밀번호가 일치하지 않습니다.");
        }

        if(changePasswordDTO.getPassword().equals(changePasswordDTO.getNewPassword())) {
            throw new PasswordNotMatchException("기존 비밀번호와 동일합니다.");
        }

        checkPasswordFormat(changePasswordDTO.getNewPassword());

        userRepository.save(user.update(User.builder()
                .password(passwordEncoder.encode(changePasswordDTO.getNewPassword()))
                .build()));

        logChangePassword(user);

        logout(email);
    }

    @Transactional
    public void resetPassword(ResetPasswordDTO resetPasswordDTO, PasswordEncoder passwordEncoder) {
        User user = userRepository.findByEmail(resetPasswordDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        checkPasswordFormat(resetPasswordDTO.getNewPassword());

        if(!requestUtil.checkVerificationSuccess(CheckSuccessCodeDTO.builder()
                .email(resetPasswordDTO.getEmail())
                .successCode(resetPasswordDTO.getSuccessCode())
                .verificationType("RESET_PASSWORD")
                .build())) {
            throw new IllegalArgumentException("인증 코드가 일치하지 않습니다.");
        }

        userRepository.save(user.update(User.builder()
                .password(passwordEncoder.encode(resetPasswordDTO.getNewPassword()))
                .build()));

        logResetPassword(user);
    }

    @Transactional
    public void deleteUser(String email, UserDeleteDTO userDeleteDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser(email);

        if(!passwordEncoder.matches(userDeleteDTO.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }

        if(requestUtil.checkAccountBalanceZero(user.getUserId())) {
            throw new IllegalArgumentException("계좌가 존재합니다.");
        }

        userRepository.save(user.delete());

        logDeleteUser(user);
    }

    private User getLoginUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    public void checkEmailFormat(String email) {
        String emailPattern =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if(!Pattern.compile(emailPattern).matcher(email).matches()) {
            throw new FromatException("이메일 형식이 올바르지 않습니다.");
        }
    }

    private void checkPasswordFormat(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@$!%*#?&])(?=\\S+$).{8,}$";

        if(!Pattern.compile(passwordPattern).matcher(password).matches()) {
            throw new FromatException("비밀번호는 영문, 숫자, 특수문자를 포함하여 8자리 이상이어야 합니다.");
        }
    }

    private UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .name(user.getName())
                .birthdate(user.getBirthdate())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
    private void logLogout(String email, boolean result) {
        log.info("===== " + "\t" +
                "로그아웃" + "\t" +
                "이메일: " + email + "\t" +
                "결과: " + result + "\t" +
                "=====");
    }

    private void logChangePassword(User user) {
        log.info("=====" + "\t" +
                "비밀번호 변경" + "\t" +
                "이메일: " + user.getEmail() + "\t" +
                "=====");
    }

    private void logResetPassword(User user) {
        log.info("=====" + "\t" +
                "비밀번호 초기화" + "\t" +
                "이메일: " + user.getEmail() + "\t" +
                "=====");
    }

    private void logDeleteUser(User user) {
        log.info("=====" + "\t" +
                "회원탈퇴" + "\t" +
                "이메일: " + user.getEmail() + "\t" +
                "=====");
    }


}

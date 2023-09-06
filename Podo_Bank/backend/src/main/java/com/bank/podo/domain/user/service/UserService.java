package com.bank.podo.domain.user.service;

import com.bank.podo.domain.user.dto.ChangePasswordDTO;
import com.bank.podo.domain.user.dto.LoginDTO;
import com.bank.podo.domain.user.dto.RegisterDTO;
import com.bank.podo.domain.user.dto.UserInfoDTO;
import com.bank.podo.domain.user.exception.AlreadyUsedUsernameException;
import com.bank.podo.domain.user.exception.PasswordFromatException;
import com.bank.podo.domain.user.exception.PasswordNotMatchException;
import com.bank.podo.domain.user.exception.UserNotFoundException;
import com.bank.podo.domain.user.repository.UserRepository;
import com.bank.podo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
        checkUsername(registerDTO.getId());

        checkPasswordFormat(registerDTO.getPassword());

        User user = toUserEntity(registerDTO, passwordEncoder);
        userRepository.save(user);
    }

    public void checkUsername(String id) {
        if(userRepository.existsById(id)) {
            throw new AlreadyUsedUsernameException("이미 사용중인 아이디입니다.");
        }
    }

    public User login(LoginDTO loginDTO, PasswordEncoder passwordEncoder) {
        User user = userRepository.findById(loginDTO.getId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디입니다."));

        if(!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    public void logout() {
    }

    public UserInfoDTO getUserInfo() {
        User user = getLoginUser();
        return toUserInfoDTO(user);
    }

    public void changePassword(ChangePasswordDTO changePasswordDTO, PasswordEncoder passwordEncoder) {
        User user = getLoginUser();

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
    }

    public void deleteUser() {
    }

    private User getLoginUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void checkPasswordFormat(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

        if(Pattern.compile(passwordPattern).matcher(password).matches()) {
            throw new PasswordFromatException("비밀번호는 영문, 숫자, 특수문자를 포함하여 8자리 이상이어야 합니다.");
        }
    }

    private User toUserEntity(RegisterDTO registerDTO, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(registerDTO.getId())
                .password(registerDTO.getPassword())
                .name(registerDTO.getName())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .address(registerDTO.getAddress())
                .gender(registerDTO.getGender())
                .birthdate(registerDTO.getBirthdate())
                .contactInfo(registerDTO.getContactInfo())
                .build();
    }

    private UserInfoDTO toUserInfoDTO(User user) {
        return UserInfoDTO.builder()
                .name(user.getName())
                .birthdate(user.getBirthdate())
                .gender(user.getGender())
                .contactInfo(user.getContactInfo())
                .address(user.getAddress())
                .id(user.getId())
                .build();
    }
}

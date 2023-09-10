package com.yongy.dotori.global.security;
// TODO : 로그인 요청을 받아서 DB에서 데이터를 조회한 뒤 결과를 전달하는 서비스 기능

import com.yongy.dotori.domain.user.entity.User;
import com.yongy.dotori.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser userDetails = null;

        try{
            User user = userRepository.findById(username);
            userDetails =  new SecurityUser(user);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("----------INVALID AUTHENTICATION----------");
        }

        return userDetails;
//        SecurityUser securityUser = new SecurityUser(); // Dto 객체 -- 추후에 고치도록
//        UserDetails build = null;
//        try{
//            User.UserBuilder userBuilder = User.withUsername(username).password(securityUser.getPassword());
//
//            userBuilder.authorities(securityUser.getAuthorities());
//            build = userBuilder.build();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return build;
    }
}

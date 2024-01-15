package com.example.oauth2practice.auth;

import com.example.oauth2practice.user.User;
import com.example.oauth2practice.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//user객체를 authenticate 객체에 넣어주기 위해
//security 설정에서 loginProcessingUrl("/login")으로 설정해놓은 것을 낚아채서
//UserDetailService에 있는 loadUserByUsername() 메서드가 실행된다 - username은 login.html에서 name="username"으로 설정해놓은 것
//동일하지 않다면 security 설정에서 usernameParameter("프론트에서 설정한 username")으로 등록
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user != null){
            return new PrincipalDetails(user); //return된 user값이 authentication안에 쏙 들어간다
        }
        return null;
    }
}

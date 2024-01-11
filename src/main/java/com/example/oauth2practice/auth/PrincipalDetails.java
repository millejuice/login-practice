package com.example.oauth2practice.auth;

import com.example.oauth2practice.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

//security가 /login 주소요청이 오면 낚아채서 대신 로그인 진행
//로그인 진행이 완료되면 시큐리티 session을 만들어줌 (Security ContextHolder)
//이 session에 들어갈 수 있는 것은 Authentication 타입 객체
//Authentication 안에 User 정보가 있어야 함
//User 오브젝트 타입 => UserDetails 타입 객체
//Security Session => Authentication => UserDetails(PrincipalDetails)
public class PrincipalDetails implements UserDetails {
    private User user; //composition
    //생성자에서 user 받아서 PrincipalDetails에 넣어준다
    public PrincipalDetails(User user){
        this.user = user;
    }
    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority(){
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collection;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        //1년 지나면 휴면계정
//        Timestamp now = new Timestamp(System.currentTimeMillis());
//        if(now.getTime() - user.getLoginDate().getTime()> 365 * 24 * 60 * 60 * 1000){
//            return false;
//        }
        return true;
    }
}

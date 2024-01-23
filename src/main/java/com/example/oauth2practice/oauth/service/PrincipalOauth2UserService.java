package com.example.oauth2practice.oauth.service;

import com.example.oauth2practice.auth.PrincipalDetails;
import com.example.oauth2practice.user.User;
import com.example.oauth2practice.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    //구글에서 받은 유저 데이타 후처리하는 함수

    @Autowired
    private UserRepo userRepo;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //userRequest에서는 구글에서 받은 유저 정보가 있다
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration()); //어떤 OAuth로 로그인했는지 확인 가능

        //구글 로그인 버튼 클릭->구글 로그인 창->로그인 후 code리턴받고 이 code를 oauth client라이브러리가 받아서 access token 요청
        //userRequest 정보 -> loadUser 함수 호출 -> 구글로부터 회원프로필 받아준다
        OAuth2User oauth2User = super.loadUser(userRequest);
        System.out.println("attributes : " + oauth2User.getAttributes());

        //회원가입 강제 진행
        String provider = userRequest.getClientRegistration().getClientId(); //google
        String providerId = oauth2User.getAttribute("sub"); //google의 primary key
        String username = provider + "_" + providerId; //google_sub -> 중복될 일 없음
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USER";

        User user= userRepo.findByUsername(username);
        if(user == null){
            user = User.builder()
                    .username(username)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepo.save(user);
        }
        //oauth login하면 user와 attributes Map을 가지고 authentication을 만들어준다
        return new PrincipalDetails(user,oauth2User.getAttributes()); //PrincipalDetails가 Authentication에 들어간다
    }
}

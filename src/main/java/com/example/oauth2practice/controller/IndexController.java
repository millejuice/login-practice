package com.example.oauth2practice.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.oauth2practice.auth.PrincipalDetails;
import com.example.oauth2practice.user.User;
import com.example.oauth2practice.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //view를 return하겠다
@RequiredArgsConstructor
public class IndexController {
    private final UserRepo userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping({"/", ""})
    public String index(){
        //mustache starter가 있으면 자동으로 src/main/resources 찾아감
        return "index";
    }
    @GetMapping("/user")
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        //일반로그인/oauth로그인 한 타입 PrincipalDetails가 전부 상속받을 수 있게 만들어야 한다
        return "user";
    }
    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }
    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }
    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }
    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }
    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword())); //회원가입 할 때 user의 pw 암호화해서 저장한다
        user.setRole("ROLE_USER"); //회원가입 할 때 user권한을 준
        userRepo.save(user);
        return "redirect:/loginForm";
    }
    @Secured("ROLE_ADMIN") //admin권한이 있어야만 실행 가능
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @GetMapping("/test/login")
    public String testLogin(Authentication authentication,
                            @AuthenticationPrincipal PrincipalDetails userDetails){ //DI로 PrincipalDetails를 받고, PrincipalDetails에는 User가 들어있다
        //PrincipatDetails이 UserDetails를 상속받고 있기 때문에 @AuthenticationPrincipal로 받을 수 있다
        System.out.println("/test/login ===================");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        //User정보를 받아오는 2가지 방법 1. Authentication 2. @AuthenticationPrincipal
        System.out.println("authentication : " + principalDetails.getUser()); //1번 : Authentication을 이용해서 User정보를 가져올 수 있다

        System.out.println("userDetails : "+userDetails.getUser()); //2번 : @AuthenticationPrincipal을 이용해서 User정보를 가져올 수 있다t

        return "세션 정보 확인하기";
    }
// Oauth login은 PrincipalDetails/UserDetails 캐스팅 받을 수 없다
    @GetMapping("/test/oauth/login")
    public String testOauthLogin(Authentication authentication){ //DI로 PrincipalDetails를 받고, PrincipalDetails에는 User가 들어있다
        System.out.println("/test/oauth/login ===================");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication : " + oAuth2User.getAttributes()); //user의 정보 Map<String,Object>로 받아온다

        return "Oauth Session Check";
    }
}

package com.example.oauth2practice.controller;

import com.example.oauth2practice.user.User;
import com.example.oauth2practice.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    public String user(){
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
}

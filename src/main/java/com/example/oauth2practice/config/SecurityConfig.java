package com.example.oauth2practice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) //controller위에 secured 어노테이션 사용 가능하게 만듦
public class SecurityConfig{
    //Bean으로 등록해준다 ? 해당 메서드 리턴되는 오브젝트 IOC로 등록
    @Bean
    public BCryptPasswordEncoder encodePwd(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        http.
                csrf().disable()
                .authorizeRequests()
                .requestMatchers("/user").authenticated() //user로 시작하는 모든 요청은 인증이 되어야함
                .requestMatchers("/manager").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //manager로 시작하는 모든 요청은 ROLE_MANAGER, ROLE_ADMIN 권한이 있어야함
                .requestMatchers("/admin").access("hasRole('ROLE_ADMIN')") //admin으로 시작하는 모든 요청은 ROLE_ADMIN 권한이 있어야함
                .anyRequest().permitAll() //그 외의 요청은 모두 허용
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login으로 호출오면 세큐리티가 낚아채서 로그인 진행
                .defaultSuccessUrl("/"); //loginForm으로 와서 로그인하면 /로 이동하는데, user로 와서 로그인하면 /user로 이동하게 설정
        return http.build();
    }
}

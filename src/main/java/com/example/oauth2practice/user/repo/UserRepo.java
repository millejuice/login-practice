package com.example.oauth2practice.user.repo;

import com.example.oauth2practice.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long>{
    //findBy까지는 규칙 그 다음이 문법
    //select * from User where username = 넘어온 parameter
    public User findByUsername(String username);
}

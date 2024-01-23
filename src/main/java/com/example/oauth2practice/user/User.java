package com.example.oauth2practice.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String provider;    //google
    private String providerId;  //google의 id 이 두개로 oauth2로 로그인한 유저인지 판단
    private Timestamp loginDate;
    @CreationTimestamp
    private Timestamp createDate;

    //Role이 두개 이상일 때 Role List로 받아서 처리
    public List<String> getRoleList(){
        if(!this.role.isEmpty()){
            return Arrays.asList(this.role.split(",")); //admin 유저일경우 ROLE_USER,ROLE_ADMIN List로 role 들어와서 split
        }
        return new ArrayList<>(); //null이면 빈 List
    }

    @Builder
    public User(String username, String password, String email, String role, String provider, String providerId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}

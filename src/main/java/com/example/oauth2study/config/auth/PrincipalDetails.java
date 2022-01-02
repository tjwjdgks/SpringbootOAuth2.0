package com.example.oauth2study.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인 진행시킨다
// 로그인 진행이 완료가 되면 시큐리티 session 정보 저장
// SecurityContextHolder 란 Authentication을 담고 있는 holder
// SecurityContextHolder는 현재 실행중인 쓰레드와 securityContext를 연결한다
// SecurityContextHolder => SecurityContext => Authentication 순으로 관리한다, 컨텍스트홀더가 컨텍스트 관리, 컨텍스트가 Authentication 객체 관리

// Authentication 안에 Account 정보가 있어야 된다
// Account type은 UserDetails 타입 객체

import com.example.oauth2study.domain.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PrincipalDetails extends User implements OAuth2User {
    Account account;
    Map<String, Object> attributes;
    public PrincipalDetails(Account account) {
        super(account.getUsername(), account.getPassword(), List.of(new SimpleGrantedAuthority(account.getRole())));
        this.account = account;
    }
    /*
    public PrincipalDetails(Map<String,Object> attributes){
        this.attributes = attributes;
    }

     */
    public Account getAccount(){
        return account;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return (String)attributes.get("sub");
    }
}

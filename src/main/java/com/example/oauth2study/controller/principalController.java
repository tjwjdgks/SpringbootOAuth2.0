package com.example.oauth2study.controller;

import com.example.oauth2study.config.auth.PrincipalDetails;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class principalController {

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

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public String info(){
        return "개인정보";
   }

    @PreAuthorize("hasRole('ROLE_MANAGER')") // data 라는 메서드가 실행되기 전에 실행된다
    @GetMapping("/data")
    public String data(){
        return "개인정보";
    }

    @GetMapping("/test/login")
    public String testLogin(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println(principal.getAccount());
        System.out.println(userDetails.getAccount());
        return "세센 정보 확인하기";
    }

    // Oauth 를 사용할경우 authentication
    @GetMapping("/test/oauth/login")
    public String testOauthLogin(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth){
        OAuth2User principal = (OAuth2User) authentication.getAuthorities();
        System.out.println(oauth.getAttributes());
        return "Oauth 세센 정보 확인하기";
    }
}

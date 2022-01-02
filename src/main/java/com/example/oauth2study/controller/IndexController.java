package com.example.oauth2study.controller;

import com.example.oauth2study.domain.Account;
import com.example.oauth2study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
@Slf4j
@RequiredArgsConstructor
public class IndexController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/")
    public String index(){
        return "index";
    }
    @GetMapping("/login")
    public String loginForm(){
        return "loginForm";
    }
    @GetMapping("/join")
    public String joinForm(){
        return "joinForm";
    }


    @PostMapping("/join")
    public String join(Account account){

        account.setRole("ROLE_USER");
        account.setLocalDateTime(LocalDateTime.now());
        String raw_password = account.getPassword();
        String enc_password= passwordEncoder.encode(raw_password);
        account.setPassword(enc_password);
        userRepository.save(account);
        return "redirect:/login";
    }

}

package com.example.oauth2study.config.auth;

import com.example.oauth2study.domain.Account;
import com.example.oauth2study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrl 요청이 오면
// 자동으로 UserDetailsService 타입으로 IOC 되어 있는 loadUserByUsername 함수가 실행된다

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Account account = userRepository.findByUsername(username);
       return new PrincipalDetails(account);
    }
}

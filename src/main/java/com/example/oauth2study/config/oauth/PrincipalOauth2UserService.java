package com.example.oauth2study.config.oauth;

import com.example.oauth2study.config.auth.PrincipalDetails;
import com.example.oauth2study.config.oauth.provider.FacebookUserInfo;
import com.example.oauth2study.config.oauth.provider.GoogleUserInfo;
import com.example.oauth2study.config.oauth.provider.NaverUserInfo;
import com.example.oauth2study.config.oauth.provider.OAuth2UserInfo;
import com.example.oauth2study.domain.Account;
import com.example.oauth2study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class PrincipalOauth2UserService  extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    // 로그인 후처리리
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
   @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println(userRequest.getClientRegistration());
        System.out.println(userRequest.getClientRegistration().getRegistrationId()); // registrationId로 어떤 Oauth에 로그인 했는지 확인가능
        System.out.println(userRequest.getAccessToken()); // access 토큰
        // 구글 로그인 버튼 -> 구글 로그인 창 -> 로그인 완료 -> code 리턴 (Oauth-client 라이브러리) -> access token 요청
       //  userRequset 정보 ->  loadUser 함수 호출 -> 회원 프로필 받아 준다


       OAuth2User oAuth2User = super.loadUser(userRequest);
       System.out.println(oAuth2User.getAttributes());
       OAuth2UserInfo oAuth2UserInfo = null;
       switch (userRequest.getClientRegistration().getRegistrationId()){
           case "google" :
               oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
               break;
           case "facebook" :
               oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
               break;
           case "naver":
               oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
               break;
           default:
               throw new RuntimeException("oauth2 provider 문제 발생");
       }
       System.out.println(oAuth2User);
       Account account = createAccount(oAuth2UserInfo);
       return new PrincipalDetails(account);

    }

    private Account createAccount(OAuth2UserInfo oAuth2UserInfo) {
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = oAuth2UserInfo.getName();
        String password = passwordEncoder.encode("default");
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";
        Account newAccount = userRepository.findByUsername(username);
        if(newAccount == null){
            newAccount = Account.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .localDateTime(LocalDateTime.now())
                    .build();
            userRepository.save(newAccount);
        }
        return newAccount;
    }
}

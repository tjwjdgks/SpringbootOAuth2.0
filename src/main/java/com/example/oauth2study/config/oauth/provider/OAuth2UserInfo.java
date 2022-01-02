package com.example.oauth2study.config.oauth.provider;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();

    default String getName(){
        return getProvider()+"_"+getProviderId();
    }
}

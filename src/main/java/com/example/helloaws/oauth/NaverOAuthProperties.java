package com.example.helloaws.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.naver")
public record NaverOAuthProperties(
        String clientId,
        String clientSecret,
        String redirectUri,
        String authorizationUri,
        String tokenUri,
        String userInfoUri
) {
}

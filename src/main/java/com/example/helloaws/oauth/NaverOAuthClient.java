package com.example.helloaws.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverOAuthClient {

    private final NaverOAuthProperties properties;
    private final RestClient restClient;

    public NaverOAuthClient(NaverOAuthProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.create();
    }

    public String getAccessToken(String code, String state) {
        String tokenRequestUri = UriComponentsBuilder.fromUriString(properties.tokenUri())
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", properties.clientId())
                .queryParam("client_secret", properties.clientSecret())
                .queryParam("code", code)
                .queryParam("state", state)
                .build()
                .toUriString();

        TokenResponse tokenResponse = restClient.post()
                .uri(tokenRequestUri)
                .retrieve()
                .body(TokenResponse.class);

        return tokenResponse.accessToken();
    }

    public NaverProfile getProfile(String accessToken) {
        return restClient.get()
                .uri(properties.userInfoUri())
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(NaverProfile.class);
    }

    public record TokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("refresh_token") String refreshToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("expires_in") String expiresIn
    ) {
    }

    public record NaverProfile(
            String resultcode,
            String message,
            Response response
    ) {
        public record Response(
                String id,
                String email,
                String name,
                String nickname
        ) {
        }
    }
}

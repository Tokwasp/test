package com.example.helloaws.oauth;

import com.example.helloaws.oauth.NaverOAuthClient.NaverProfile;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
public class NaverOAuthController {

    private static final String STATE_SESSION_KEY = "oauth_state";

    private final NaverOAuthProperties properties;
    private final NaverOAuthClient naverOAuthClient;

    public NaverOAuthController(NaverOAuthProperties properties, NaverOAuthClient naverOAuthClient) {
        this.properties = properties;
        this.naverOAuthClient = naverOAuthClient;
    }

    @GetMapping("/oauth/naver")
    public RedirectView login(HttpSession session) {
        String state = UUID.randomUUID().toString();
        session.setAttribute(STATE_SESSION_KEY, state);

        String authorizationUri = UriComponentsBuilder.fromUriString(properties.authorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("state", state)
                .build()
                .toUriString();

        return new RedirectView(authorizationUri);
    }

    @GetMapping("/oauth/naver/callback")
    public NaverProfile.Response callback(
            @RequestParam String code,
            @RequestParam String state,
            HttpSession session
    ) {
        verifyState(session, state);

        String accessToken = naverOAuthClient.getAccessToken(code, state);
        NaverProfile profile = naverOAuthClient.getProfile(accessToken);

        return profile.response();
    }

    private void verifyState(HttpSession session, String state) {
        String savedState = (String) session.getAttribute(STATE_SESSION_KEY);
        session.removeAttribute(STATE_SESSION_KEY);

        if (savedState == null || !savedState.equals(state)) {
            throw new IllegalStateException("네이버 로그인 state 값이 일치하지 않습니다.");
        }
    }
}

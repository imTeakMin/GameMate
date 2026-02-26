package com.example.gamescord.security;

import com.example.gamescord.service.refreshtoken.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.oauth2.authorized-redirect-uri}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 1. JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername()).getToken();

        // 2. 신규 유저 여부 판단 (생년월일 정보가 없으면 신규 유저로 간주)
        boolean isNewUser = userDetails.getUser().getUsersBirthday() == null;

        // 3. 프론트엔드로 전달할 URL 구성 (쿼리 파라미터로 토큰 및 신규 유저 여부 전달)
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("isNewUser", isNewUser)
                .build().toUriString();

        // 4. 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

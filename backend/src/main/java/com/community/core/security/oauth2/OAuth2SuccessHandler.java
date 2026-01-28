package com.community.core.security.oauth2;

import com.community.core.security.jwt.JwtProperties;
import com.community.core.security.jwt.JwtProvider;
import com.community.domain.auth.service.RefreshTokenService;
import com.community.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.oauth2.redirect-url:http://localhost:3000/oauth2/callback}")
    private String redirectUrl;

    /**
     *  OAuth2 인증 성공 처리
     * @param request  request
     * @param response response
     * @param authentication authentication
     * @throws IOException  IOException
     * @throws ServletException ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // PrincipalUser에서 User 정보 추출
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        User user = principalUser.getUser();
        log.info("[OAuth2] 로그인 성공: userId={}, email={}", user.getId(), user.getEmail());

        // JWT 토큰 생성
        String accessToken =  jwtProvider.createAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // Refresh Token을 Redis에 저장
        refreshTokenService.saveRefreshToken(user.getId(), refreshToken);

        // 프론트로 리다이렉트
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("access_token", accessToken)
                .queryParam("refresh_token", refreshToken)
                .queryParam("expiresIn",jwtProperties.getAccessTokenValidity())
                .build()
                .toUriString();
        log.info("[OAuth2] 토큰 발급 완료, 리다이렉트: url={}", redirectUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);


    }
}

package com.community.domain.auth.service;

import com.community.core.exception.ErrorCode;
import com.community.core.exception.custom.UnauthorizedException;
import com.community.core.security.jwt.JwtProperties;
import com.community.core.security.jwt.JwtProvider;
import com.community.domain.auth.dto.request.LoginRequest;
import com.community.domain.auth.dto.request.TokenRefreshRequest;
import com.community.domain.auth.dto.response.TokenResponse;
import com.community.domain.user.entity.User;
import com.community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JwtProperties jwtProperties;

    /**
     * 로그인
     * @param request 이메일 비밀번호
     * @return JWT
     */
    @Transactional
    public TokenResponse login(LoginRequest request){
        //사용자 조회
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.LOGIN_FAILED));

        //이메일 검증
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new UnauthorizedException(ErrorCode.LOGIN_FAILED);
        }

        user.updateLastLoginAt();
        String accessToken = jwtProvider.createAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        log.info("[AUTH] 로그인 성공: userId={}, email={}", user.getId(), user.getEmail());

        return TokenResponse.of(
                accessToken,
                refreshToken,
                jwtProperties.getAccessTokenValidity()
        );
    }

    /**
     *  토큰 갱신
     * @param request 토큰 갱신 요청
     * @return JWT
     */
    public TokenResponse refresh(TokenRefreshRequest request){
        String refreshToken = request.getRefreshToken();

        //refresh Token 검증
        if(!jwtProvider.validateToken(refreshToken)){
            throw new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        //refresh Token 에서 사용자 ID 추출
        Long userId = jwtProvider.getUserIdFromToken(refreshToken);

        //사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException(ErrorCode.INVALID_REFRESH_TOKEN));

        String newAccessToken = jwtProvider.createAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());
        log.info("[AUTH] 토큰 갱신 성공: userId={}", user.getId());

        return TokenResponse.of(
                newAccessToken,
                newRefreshToken,
                jwtProperties.getAccessTokenValidity()
        );

    }
}

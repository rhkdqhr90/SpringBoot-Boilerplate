package com.community.domain.auth.controller;

import com.community.core.common.dto.ApiResponse;
import com.community.domain.auth.dto.request.LoginRequest;
import com.community.domain.auth.dto.request.TokenRefreshRequest;
import com.community.domain.auth.dto.response.TokenResponse;
import com.community.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request){
        log.info("[API] 로그인 요청: email={}", request.getEmail());

        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(@Valid @RequestBody TokenRefreshRequest request){
        log.info("[API] 토큰 갱신 요청");

        TokenResponse response = authService.refresh(request);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(){
        log.info("[API] 로그아웃 요청");
        return ResponseEntity.ok(ApiResponse.success("로그아웃되었습니다."));
    }
}

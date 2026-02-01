package com.community.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 보안 경로 설정
 * application.yml의 security.paths 프리픽스와 바인딩
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.paths")
public class SecurityPathProperties {

    /**
     * 인증 없이 접근 가능한 경로 (모든 HTTP 메서드)
     */
    private List<String> permitAll = new ArrayList<>();

    /**
     * GET 요청만 인증 없이 접근 가능한 경로
     */
    private List<String> permitGetOnly = new ArrayList<>();

    /**
     * POST 요청만 인증 없이 접근 가능한 경로
     */
    private List<String> permitPostOnly = new ArrayList<>();

    /**
     * ADMIN 권한이 필요한 경로
     */
    private List<String> adminOnly = new ArrayList<>();

    /**
     * BeCrypt 암호화 강도 (기본값: 12)
     */
    private int passwordStrength = 12;
}

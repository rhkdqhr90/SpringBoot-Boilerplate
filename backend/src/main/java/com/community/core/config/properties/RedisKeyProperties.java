package com.community.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis 키 프리픽스 설정
 * application.yml의 redis.key-prefix 프리픽스와 바인딩
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "redis.key-prefix")
public class RedisKeyProperties {

    /**
     * Refresh Token 저장 키 프리픽스
     */
    private String refreshToken = "refresh_token:";

    /**
     * 캐시 키 프리픽스
     */
    private String cache = "cache:";

    /**
     * 세션 키 프리픽스
     */
    private String session = "session:";

    /**
     * 이메일 인증 코드 키 프리픽스
     */
    private String emailVerification = "email_verification:";
}

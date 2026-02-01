package com.community.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션 전반 설정
 * application.yml의 app 프리픽스와 바인딩
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private User user = new User();
    private OAuth2 oauth2 = new OAuth2();

    @Getter
    @Setter
    public static class User {
        /**
         * 닉네임 변경 제한 일수 (기본값: 30일)
         */
        private int nicknameChangeIntervalDays = 30;
    }

    @Getter
    @Setter
    public static class OAuth2 {
        /**
         * OAuth2 성공 후 프론트엔드 리다이렉트 URL
         */
        private String redirectUrl = "http://localhost:3000/oauth2/callback";
    }
}

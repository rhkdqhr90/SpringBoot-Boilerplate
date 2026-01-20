package com.community.core.security.oauth2;

import java.util.Map;

public interface OAuthUserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
    String getNickname();
    String getProfileImage();
    Map<String, Object> getAttributes();


    static OAuthUserInfo of(String provider, Map<String, Object> attributes) {
        return switch (provider.toLowerCase()){
            case "google" -> new GoogleOAuthUserInfo(attributes);
            case "kakao" -> new KakaoOAuthUserInfo(attributes);
            case "naver" -> new NaverOAuthUserInfo(attributes);
            default -> throw new IllegalArgumentException("지원하지 않는 OAuth Provider입니다");
        };
    }

}

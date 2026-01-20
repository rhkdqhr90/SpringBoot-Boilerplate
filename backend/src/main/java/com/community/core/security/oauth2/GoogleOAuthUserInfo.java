package com.community.core.security.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Google OAuth2 사용자 정보
 *
 * <p>Google 응답 구조:</p>
 * <pre>
 * {
 *   "sub": "123456789",
 *   "email": "user@gmail.com",
 *   "name": "홍길동",
 *   "picture": "https://..."
 * }
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public class GoogleOAuthUserInfo implements OAuthUserInfo{
    private final Map<String, Object> attributes;


    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getProfileImage() {
        return (String) attributes.get("picture");
    }
}

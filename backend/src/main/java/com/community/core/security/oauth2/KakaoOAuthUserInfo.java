package com.community.core.security.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Kakao OAuth2 사용자 정보
 *
 * <p>Kakao 응답 구조:</p>
 * <pre>
 * {
 *   "id": 123456789,
 *   "kakao_account": {
 *     "email": "user@kakao.com",
 *     "profile": {
 *       "nickname": "홍길동",
 *       "profile_image_url": "https://..."
 *     }
 *   }
 * }
 * </pre>
 */
@Getter
@RequiredArgsConstructor
public class KakaoOAuthUserInfo implements OAuthUserInfo{

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = getKakaoAccount();
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getNickname() {
        Map<String, Object> profile = getProfile();
        return (String) profile.get("nickname");
    }

    @Override
    public String getProfileImage() {
        Map<String, Object> profile = getProfile();
        return (String) profile.get("profile_image_url");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getKakaoAccount() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getProfile() {
        Map<String, Object> kakaoAccount = getKakaoAccount();
        return (Map<String, Object>) kakaoAccount.get("profile");
    }
}

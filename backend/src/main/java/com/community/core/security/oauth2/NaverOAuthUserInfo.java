package com.community.core.security.oauth2;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * Naver OAuth2 사용자 정보
 *
 * <p>Naver 응답 구조:</p>
 * <pre>
 * {
 *   "resultcode": "00",
 *   "message": "success",
 *   "response": {
 *     "id": "123456789",
 *     "email": "user@naver.com",
 *     "name": "홍길동",
 *     "profile_image": "https://..."
 *   }
 * }
 * </pre>
 */

@Getter
@RequiredArgsConstructor
public class NaverOAuthUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    @Override
    public String getProviderId() {
        Map<String, Object> response = getResponse();
        return (String) response.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = getResponse();
        return (String) response.get("email");
    }

    @Override
    public String getNickname() {
        Map<String, Object> response = getResponse();
        return (String) response.get("name");
    }

    @Override
    public String getProfileImage() {
        Map<String, Object> response = getResponse();
        return (String) response.get("profile_image");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getResponse() {
        return (Map<String, Object>) attributes.get("response");
    }
}

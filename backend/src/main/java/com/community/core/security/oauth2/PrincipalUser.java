package com.community.core.security.oauth2;

import com.community.domain.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class PrincipalUser implements OAuth2User {
    private final User user;
    private final Map<String, Object> attributes;

    /**
     * OAuth Provider로부터 받은 사용자 속성
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getName() {
        return String.valueOf(user.getId());
    }

    public Long getUserId(){
        return user.getId();
    }




}

package com.community.core.security.oauth2;

import com.community.domain.auth.repository.OAuthAccountRepository;
import com.community.domain.user.entity.OAuthAccount;
import com.community.domain.user.entity.Role;
import com.community.domain.user.entity.User;
import com.community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService{
    private final UserRepository userRepository;
    private final OAuthAccountRepository oAuthAccountRepository;


    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //OAuth Provider로부터 사용자 정보 받기
        OAuth2User oAuth2User = super.loadUser(userRequest);

        //Provider 정보 및 사용자 정보 추출
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuthUserInfo userInfo = OAuthUserInfo.of(provider, oAuth2User.getAttributes());

        log.info("[OAuth2] 로그인 시도: provider={}, email={}", provider, userInfo.getEmail());

        //이메일로 기존 사용자 찾기 (없으면 생성)
        User user = userRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> createUser(userInfo));

        // OAuth 계정 연결 (없으면 생성)
        linkOAuthAccount(user, provider, userInfo);

        //마지막 로그인 시각 업데이트
        user.updateLastLoginAt();

        return new PrincipalUser(user, oAuth2User.getAttributes());
    }

    private User createUser(OAuthUserInfo userInfo) {
        User user = User.createOAuthUser(
                userInfo.getEmail(),
                generateUniqueNickname(userInfo.getNickname()),
                userInfo.getProfileImage()
        );
        userRepository.save(user);

        log.info("[OAuth2] 신규 사용자 생성: userId={}, email={}", user.getId(), user.getEmail());

        return user;

    }

    /**
     *  OAuth 계정 연걸
     * @param user 사용자
     * @param provider provider
     * @param userInfo 유저 정보
     */
    private  void linkOAuthAccount(User user, String provider, OAuthUserInfo userInfo) {
        boolean exists  = oAuthAccountRepository.existsByUserAndProvider(user, provider);
        if(!exists){
            OAuthAccount account = OAuthAccount.of(user, provider, userInfo.getProviderId(), userInfo.getEmail());
            oAuthAccountRepository.save(account);
            log.info("[OAuth2] OAuth 계정 연결: userId={}, provider={}", user.getId(), provider);
        }
    }

    /**
     * 고유 닉네임 생성 (숫자 하나씩 증가)
     * @param baseNickname provider nickname
     * @return 고유 닉네임
     */
    private String generateUniqueNickname(String baseNickname) {
        String nickname = baseNickname;
        int suffix = 1;

        while (userRepository.existsByNickname(nickname)) {
            nickname = baseNickname + suffix++;
        }

        return nickname;
    }
}

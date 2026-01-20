package com.community.domain.auth.repository;

import com.community.domain.user.entity.OAuthAccount;
import com.community.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {
    /**
     * Provider 과 ProviderId로 OAuth 계정 조회
     * @param provider provider
     * @param providerId Id
     * @return OAuthAccount
     */
    Optional<OAuthAccount> findByProviderAndProviderId(String provider, String providerId);

    /**
     * 사용자 provider OAuth 계정 조회
     * @param user 사용자
     * @param provider provider
     * @return OAuthAccount
     */
    Optional<OAuthAccount> findByUserAndProvider(User user, String provider);

    /**
     * OAuth 계정 존재여부
     * @param provider provider
     * @param providerId Id
     * @return boolean
     */
    boolean existsByProviderAndProviderId(String provider, String providerId);

    /**
     * 사용자와 Provider로 OAuth 계정 확인
     * @param user 사용지
     * @param provider Provider
     * @return boolean
     */
    boolean existsByUserAndProvider(User user, String provider);
}

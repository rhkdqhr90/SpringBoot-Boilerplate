package com.community.core.config;

import com.community.core.security.jwt.JwtAuthenticationFilter;
import com.community.core.security.oauth2.CustomOAuth2UserService;
import com.community.core.security.oauth2.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    /**
     * 비밀 번호 암호화 인코더
     * @return BCryptPasswordEncoder(strength=12)
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     return http
                //Csrf 비활성화
                .csrf(csrf -> csrf.disable())
                //세션 사용 안함
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //모든 요청 허용(임시)
                .authorizeHttpRequests(auth -> auth
                        // 공개 API
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/boards/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()

                        // 관리자 API
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")

                        // 나머지는 인증 필요
                        .anyRequest().authenticated())
                //OAuth2 로그인
                .oauth2Login(oauth2 -> oauth2
                     // OAuth2 로그인 페이지 경로 (프론트엔드로 리다이렉트)
                     .authorizationEndpoint(authEndpoint -> authEndpoint.baseUri("/api/v1/auth/oauth2/authorization"))
                     //OAuth2 콜백 경로
                     .redirectionEndpoint(redEndpoint -> redEndpoint.baseUri("/api/v1/auth/oauth2/redirect"))
                     //사용자 정보 로드
                     .userInfoEndpoint(userinfo -> userinfo.userService(customOAuth2UserService))
                     //성공 핸들러(JWT)
                     .successHandler(oAuth2SuccessHandler))
                //jwt 필터 추가
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }
}

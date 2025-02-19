package com.edu.springboot;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    public SecurityConfig(OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 보안 해제
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/oauth2/**", "/api/**").permitAll() // 로그인 페이지 허용
                .anyRequest().authenticated() // 나머지 요청은 인증 필요
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/home", true) // 로그인 성공 후 이동할 URL
                .failureUrl("/login?error=true") // 로그인 실패 시 이동할 URL
                .userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService)) // 🔥 수정됨
            )
            .formLogin(form -> form
                .loginPage("/login") // 직접 만든 로그인 페이지 사용
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }
}


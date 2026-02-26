package com.example.gamescord.security;

import com.example.gamescord.domain.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Spring Security 의 UserDetails 및 OAuth2User 인터페이스를 구현한 커스텀 클래스입니다.
 * 인증된 사용자의 상세 정보(권한, 비밀번호, 아이디, 소셜 정보 등)를 캡슐화합니다.
 */
@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {

    // 애플리케이션의 도메인 모델인 User 객체를 포함합니다.
    private final User user;
    private Map<String, Object> attributes;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    /**
     * OAuth2User 인터페이스의 메소드로, 소셜 서비스로부터 받은 속성들을 반환합니다.
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * @return 사용자의 고유 ID를 반환합니다.
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * 사용자가 가진 권한 목록을 반환합니다.
     * @return 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * 사용자의 비밀번호를 반환합니다.
     * @return 암호화된 비밀번호
     */
    @Override
    public String getPassword() {
        return user.getLoginPwd();
    }

    /**
     * 사용자의 아이디를 반환합니다.
     * @return 로그인 아이디
     */
    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    /**
     * OAuth2User 인터페이스의 메소드로, 유저의 이름을 반환합니다 (식별값으로 사용).
     */
    @Override
    public String getName() {
        return String.valueOf(user.getId());
    }
}
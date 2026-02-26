package com.example.gamescord.security;

import com.example.gamescord.domain.User;
import com.example.gamescord.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 구글에서 제공하는 정보들
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google"
        String providerId = oAuth2User.getAttribute("sub"); // 구글 고유 ID
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        // 유저 찾기 또는 생성
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;
        
        if (userOptional.isPresent()) {
            user = userOptional.get();
            userRepository.saveUser(user);
        } else {
            // 신규 가입
            user = new User();
            user.setLoginId(email); // 소셜 유저는 이메일을 loginId로 사용 (충돌 방지)
            user.setEmail(email);
            user.setUsersName(name != null ? name : "Social User");
            user.setPoint(0L);
            user.setLoginFailCount(0);
            user.setEnabled(true);
            
            userRepository.saveUser(user);
        }

        return new CustomUserDetails(user, attributes);
    }
}

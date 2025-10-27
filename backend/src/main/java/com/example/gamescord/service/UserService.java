package com.example.gamescord.service;

import com.example.gamescord.domain.User;
import com.example.gamescord.dto.UserLoginRequestDTO;
import com.example.gamescord.dto.UserSignupRequestDTO;
import com.example.gamescord.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User signup(UserSignupRequestDTO requestDto) {
        // 로그인 ID 중복 확인
        if (userRepository.findByLoginId(requestDto.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getLoginPwd());

        User newUser = new User();
        newUser.setLoginId(requestDto.getLoginId());
        newUser.setLoginPwd(encodedPassword);
        newUser.setUsersName(requestDto.getUsersName());
        newUser.setUsersBirthday(requestDto.getUsersBirthday());
        newUser.setGender(requestDto.getGender());
        
        // 자기소개는 DTO에 있는 경우에만 설정
        if (requestDto.getUsersDescription() != null) {
            newUser.setUsersDescription(requestDto.getUsersDescription());
        }
        
        // 초기 포인트 및 로그인 실패 횟수 설정
        newUser.setPoint(0L);
        newUser.setLoginFailCount(0);

        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User login(UserLoginRequestDTO requestDto) {
        // 사용자가 존재하는지 확인
        User user = userRepository.findByLoginId(requestDto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("등록된 사용자가 없습니다."));

        // 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(requestDto.getLoginPwd(), user.getLoginPwd())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }
}
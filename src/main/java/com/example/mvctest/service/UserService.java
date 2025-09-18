package com.example.mvctest.service;

import com.example.mvctest.domain.User;
import com.example.mvctest.dto.UserLoginRequestDTO;
import com.example.mvctest.dto.UserResponseDTO;
import com.example.mvctest.dto.UserSignupRequestDTO;
import com.example.mvctest.repository.userrepository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(UserSignupRequestDTO requestDto) {
        // Check for duplicate loginId
        if (userRepositoryPort.findByLoginId(requestDto.getLoginId()).isPresent()) {
            throw new IllegalArgumentException("Login ID already exists: " + requestDto.getLoginId());
        }

        User user = new User();
        user.setLoginId(requestDto.getLoginId());
        user.setLoginPwd(passwordEncoder.encode(requestDto.getLoginPwd()));
        user.setUsersName(requestDto.getUsersName());
        user.setUsersDescription(requestDto.getUsersDescription());
        user.setUsersBirthday(requestDto.getUsersBirthday());
        user.setPoint(0L); // Initialize points to 0

        userRepositoryPort.save(user);
    }

    public UserResponseDTO login(UserLoginRequestDTO requestDto) {
        User user = userRepositoryPort.findByLoginId(requestDto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + requestDto.getLoginId()));

        if (!passwordEncoder.matches(requestDto.getLoginPwd(), user.getLoginPwd())) {
            throw new IllegalArgumentException("Invalid password");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getLoginId())
                .password(user.getLoginPwd())
                .authorities(new ArrayList<>())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return UserResponseDTO.builder()
                .loginId(user.getLoginId())
                .usersName(user.getUsersName())
                .point(user.getPoint())
                .usersDescription(user.getUsersDescription())
                .usersBirthday(user.getUsersBirthday())
                .build();
    }
}

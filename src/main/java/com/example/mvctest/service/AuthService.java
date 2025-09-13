package com.example.mvctest.service;

import com.example.mvctest.domain.User;
import com.example.mvctest.dto.UserLoginRequestDTO;
import com.example.mvctest.dto.UserResponseDTO;
import com.example.mvctest.repository.userrepository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryPort userRepositoryPort;

    public UserResponseDTO login(UserLoginRequestDTO requestDto) {
        User user = userRepositoryPort.findByLoginId(requestDto.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with loginId: " + requestDto.getLoginId()));

        if (!user.getLoginPwd().equals(requestDto.getLoginPwd())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return UserResponseDTO.builder()
                .loginId(user.getLoginId())
                .usersName(user.getUsersName())
                .point(user.getPoint())
                .usersDescription(user.getUsersDescription())
                .usersBirthday(user.getUsersBirthday())
                .build();
    }
}

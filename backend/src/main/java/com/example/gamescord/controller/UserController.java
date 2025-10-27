package com.example.gamescord.controller;

import com.example.gamescord.domain.User;
import com.example.gamescord.dto.UserLoginRequestDTO;
import com.example.gamescord.dto.UserResponseDTO;
import com.example.gamescord.dto.UserSignupRequestDTO;
import com.example.gamescord.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signUp(@Valid @RequestBody UserSignupRequestDTO requestDto) {
        User newUser = userService.signup(requestDto);

        UserResponseDTO responseDto = UserResponseDTO.builder()
                .id(newUser.getId())
                .loginId(newUser.getLoginId())
                .point(newUser.getPoint())
                .usersName(newUser.getUsersName())
                .usersDescription(newUser.getUsersDescription())
                .usersBirthday(newUser.getUsersBirthday())
                .gender(newUser.getGender())
                .profileImageUrl(newUser.getProfileImageUrl())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserLoginRequestDTO requestDto, HttpServletRequest request) {
        User user = userService.login(requestDto);

        // 세션이 있으면 가져오고, 없으면 새로 생성
        HttpSession session = request.getSession(true);
        // 세션에 로그인한 사용자의 ID를 저장
        session.setAttribute("userId", user.getId());

        UserResponseDTO responseDto = UserResponseDTO.builder()
                .id(user.getId())
                .loginId(user.getLoginId())
                .point(user.getPoint())
                .usersName(user.getUsersName())
                .usersDescription(user.getUsersDescription())
                .usersBirthday(user.getUsersBirthday())
                .gender(user.getGender())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Spring Security의 logout 핸들러가 이 호출을 처리
        return ResponseEntity.ok("Logged out successfully");
    }
}

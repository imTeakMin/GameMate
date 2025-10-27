package com.example.gamescord.controller;

import com.example.gamescord.domain.User;
import com.example.gamescord.dto.user.UserLoginRequestDTO;
import com.example.gamescord.dto.user.UserResponseDTO;
import com.example.gamescord.dto.user.UserSignupRequestDTO;
import com.example.gamescord.service.user.UserService;
 
 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.gamescord.dto.user.UserProfileUpdateRequestDTO;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import com.example.gamescord.service.user.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

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
        // 1. 사용자 존재 여부 먼저 확인 (등록된 사용자가 없는 경우 여기서 예외 발생)
        User user = userService.getUserByLoginId(requestDto.getLoginId());

        // 2. 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                requestDto.getLoginId(), requestDto.getLoginPwd()
        );

        Authentication authentication;
        try {
            // 3. AuthenticationManager 를 통해 인증 시도 (사용자 존재 확인 후이므로 BadCredentialsException 은 비밀번호 불일치)
            authentication = authenticationManager.authenticate(authToken);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 4. SecurityContextHolder 에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 5. SecurityContext 를 HttpSession 에 저장
        SecurityContext context = SecurityContextHolder.getContext();
        securityContextRepository.saveContext(context, request, null);

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
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        // Clear the SecurityContextHolder to ensure no residual authentication
        SecurityContextHolder.clearContext();
        // Invalidate session and delete cookies are handled by SecurityContextLogoutHandler
        return ResponseEntity.ok("{\"message\": \"Logged out successfully\"}");
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserResponseDTO> updateUserProfile(
            @Valid @RequestBody UserProfileUpdateRequestDTO requestDto) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        if (userId == null) { // Redundant check, but kept for consistency
            throw new IllegalArgumentException("로그인된 사용자만 프로필을 수정할 수 있습니다.");
        }

        User updatedUser = userService.updateUserProfile(userId, requestDto);

        UserResponseDTO responseDto = UserResponseDTO.builder()
                .id(updatedUser.getId())
                .loginId(updatedUser.getLoginId())
                .point(updatedUser.getPoint())
                .usersName(updatedUser.getUsersName())
                .usersDescription(updatedUser.getUsersDescription())
                .usersBirthday(updatedUser.getUsersBirthday())
                .gender(updatedUser.getGender())
                .profileImageUrl(updatedUser.getProfileImageUrl())
                .build();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDTO> getUserProfile() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        if (userId == null) {
            throw new IllegalArgumentException("로그인된 사용자만 프로필을 조회할 수 있습니다.");
        }

        User user = userService.getUserProfile(userId);

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
}

package com.example.gamescord.controller;

import com.example.gamescord.domain.User;
import com.example.gamescord.dto.UserLoginRequestDTO;
import com.example.gamescord.dto.UserResponseDTO;
import com.example.gamescord.dto.UserSignupRequestDTO;
import com.example.gamescord.service.user.UserService;
 
 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.gamescord.dto.UserProfileUpdateRequestDTO;
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
        // 1. 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                requestDto.getLoginId(), requestDto.getLoginPwd()
        );

        // 2. AuthenticationManager를 통해 인증 시도
        Authentication authentication = authenticationManager.authenticate(authToken);

        // 3. SecurityContextHolder에 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. SecurityContext를 HttpSession에 저장 (세션 기반 인증의 핵심)
        SecurityContext context = SecurityContextHolder.getContext();
        securityContextRepository.saveContext(context, request, null);

        // 5. 인증된 사용자 정보 가져오기 (여기서는 UserDetails 객체에서 사용자 ID를 가져옴)
        // UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        // Long userId = ((User) userDetails).getId(); // Assuming User implements UserDetails and has getId()

        // Note: The original userService.login(requestDto) was used to get the User object.
        // We can still call it here to get the full User object for the response DTO,
        // or modify UserService.login to return UserDetails and then cast/convert.
        // For now, let's keep the userService.login call to get the full User object for the response.
        User user = userService.login(requestDto); // This call will now effectively re-verify credentials, or can be refactored.
                                                  // A better approach would be to get the User object from the Authentication principal
                                                  // if User implements UserDetails and contains the full User data.
                                                  // For simplicity and to maintain existing response structure, keeping this for now.


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
        // The check for userId == null is technically redundant here if principal is always CustomUserDetails
        // and authentication is guaranteed by Spring Security.
        // However, keeping it for defensive programming or if principal could be anonymous.
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

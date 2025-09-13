package com.example.mvctest.controller;

import com.example.mvctest.dto.UserLoginRequestDTO;
import com.example.mvctest.dto.UserSignupRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody UserSignupRequestDTO userSignupRequestDTO){
        return ResponseEntity.ok(Collections.singletonMap("message", "회원가입 성공"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO){
        return ResponseEntity.ok(Collections.singletonMap("message", "로그인 성공"));
    }
}

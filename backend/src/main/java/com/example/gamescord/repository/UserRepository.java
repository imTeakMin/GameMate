package com.example.gamescord.repository;

import com.example.gamescord.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 로그인 ID로 사용자를 찾는 메서드 (회원가입 시 중복 체크에 사용)
    Optional<User> findByLoginId(String loginId);
}

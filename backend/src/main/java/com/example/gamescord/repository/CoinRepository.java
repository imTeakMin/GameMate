package com.example.gamescord.repository;

import com.example.gamescord.domain.Coin;
import com.example.gamescord.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
    List<Coin> findByUsersOrderByCreatedAtDesc(User user);
}

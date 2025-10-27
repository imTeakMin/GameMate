package com.example.gamescord.service.coin;

import com.example.gamescord.domain.Coin;
import com.example.gamescord.domain.User;
import com.example.gamescord.dto.coin.CoinChargeRequestDTO;
import com.example.gamescord.dto.coin.CoinHistoryResponseDTO;
import com.example.gamescord.repository.CoinRepository;
import com.example.gamescord.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CoinService {

    private final UserRepository userRepository;
    private final CoinRepository coinRepository;

    private static final Map<Integer, Integer> COIN_PRICE_MAP = Map.of(
            500, 4680,
            1000, 9360,
            2000, 18720,
            5000, 45600,
            10000, 91200,
            30000, 266400,
            50000, 444000,
            100000, 864000,
            300000, 2592000,
            500000, 4320000
    );

    @Transactional
    public void chargeCoin(Long userId, CoinChargeRequestDTO requestDto) {
        Integer coinAmount = requestDto.getCoinAmount();
        if (!COIN_PRICE_MAP.containsKey(coinAmount)) {
            throw new IllegalArgumentException("유효하지 않은 충전 단위입니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setPoint(user.getPoint() + coinAmount);
        userRepository.save(user);

        Coin newCoin = new Coin();
        newCoin.setUsers(user);
        newCoin.setCoinAmount(coinAmount);
        newCoin.setPaymentAmount(COIN_PRICE_MAP.get(coinAmount));
        newCoin.setPaymentMethod("SIMULATED_PAYMENT");
        newCoin.setCreatedAt(Instant.now());
        coinRepository.save(newCoin);
    }

    @Transactional(readOnly = true)
    public List<CoinHistoryResponseDTO> getCoinHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Coin> coinHistory = coinRepository.findByUsersOrderByCreatedAtDesc(user);

        return coinHistory.stream()
                .map(CoinHistoryResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void refundCoin(Long userId, Long coinId) {
        Coin coin = coinRepository.findById(coinId)
                .orElseThrow(() -> new IllegalArgumentException("충전 내역을 찾을 수 없습니다."));

        if (!coin.getUsers().getId().equals(userId)) {
            throw new IllegalArgumentException("본인의 충전 내역만 환불할 수 있습니다.");
        }

        User user = coin.getUsers();

        long newPoint = user.getPoint() - coin.getCoinAmount();
        if (newPoint < 0) {
            throw new IllegalStateException("포인트가 부족하여 환불할 수 없습니다.");
        }
        user.setPoint(newPoint);
        userRepository.save(user);

        coinRepository.delete(coin);
    }
}

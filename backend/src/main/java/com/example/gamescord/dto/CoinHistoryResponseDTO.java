package com.example.gamescord.dto;

import com.example.gamescord.domain.Coin;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CoinHistoryResponseDTO {
    private Long coinId;
    private Integer coinAmount;
    private Integer paymentAmount;
    private String paymentMethod;
    private Instant createdAt;

    public static CoinHistoryResponseDTO fromEntity(Coin coin) {
        return CoinHistoryResponseDTO.builder()
                .coinId(coin.getId())
                .coinAmount(coin.getCoinAmount())
                .paymentAmount(coin.getPaymentAmount())
                .paymentMethod(coin.getPaymentMethod())
                .createdAt(coin.getCreatedAt())
                .build();
    }
}

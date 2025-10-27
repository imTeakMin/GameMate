package com.example.gamescord.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinChargeRequestDTO {

    @NotNull(message = "충전할 코인 양은 필수입니다.")
    private Integer coinAmount;
}

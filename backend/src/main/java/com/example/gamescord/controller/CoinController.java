package com.example.gamescord.controller;

import com.example.gamescord.dto.coin.CoinChargeRequestDTO;
import com.example.gamescord.dto.coin.CoinHistoryResponseDTO;
import com.example.gamescord.service.coin.CoinService;
import com.example.gamescord.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coins")
@RequiredArgsConstructor
public class CoinController {

    private final CoinService coinService;

    @PostMapping("/charge")
    public ResponseEntity<String> chargeCoin(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CoinChargeRequestDTO requestDto) {
        coinService.chargeCoin(userDetails.getId(), requestDto);
        return ResponseEntity.ok("코인이 성공적으로 충전되었습니다.");
    }

    @GetMapping("/history")
    public ResponseEntity<List<CoinHistoryResponseDTO>> getCoinHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<CoinHistoryResponseDTO> history = coinService.getCoinHistory(userDetails.getId());
        return ResponseEntity.ok(history);
    }

    @DeleteMapping("/{coinId}/refund")
    public ResponseEntity<String> refundCoin(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long coinId) {
        coinService.refundCoin(userDetails.getId(), coinId);
        return ResponseEntity.ok("성공적으로 환불되었습니다.");
    }
}

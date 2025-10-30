package com.example.gamescord.controller;

import com.example.gamescord.dto.match.MatchRequestDTO;
import com.example.gamescord.dto.match.MatchResponseDTO;
import com.example.gamescord.dto.match.MatchStatusUpdateRequestDTO;
import com.example.gamescord.security.CustomUserDetails;
import com.example.gamescord.service.match.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchResponseDTO> requestMatch(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody MatchRequestDTO requestDto) {
        MatchResponseDTO responseDto = matchService.requestMatch(userDetails.getId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PatchMapping("/{matchId}/status")
    public ResponseEntity<MatchResponseDTO> updateMatchStatus(
            @PathVariable Long matchId,
            @Valid @RequestBody MatchStatusUpdateRequestDTO requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        MatchResponseDTO responseDto = matchService.updateMatchStatus(matchId, requestDto, userDetails.getId());
        return ResponseEntity.ok(responseDto);
    }
}
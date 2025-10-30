package com.example.gamescord.dto.match;

import com.example.gamescord.domain.Match;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchResponseDTO {

    private Long matchId;
    private Long requesterId;
    private Long gamematePlayerId;
    private Long gameId;
    private String status;

    public static MatchResponseDTO fromEntity(Match match) {
        return MatchResponseDTO.builder()
                .matchId(match.getId())
                .requesterId(match.getOrderUsersId())
                .gamematePlayerId(match.getOrderedUsersId())
                .gameId(match.getOrdersGameId())
                .status(match.getOrderStatus())
                .build();
    }
}

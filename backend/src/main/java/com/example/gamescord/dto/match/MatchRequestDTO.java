package com.example.gamescord.dto.match;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchRequestDTO {

    @NotNull(message = "상대방의 사용자 ID는 필수입니다.")
    private Long orderedUserId;

    @NotNull(message = "게임 ID는 필수입니다.")
    private Long gameId;
}

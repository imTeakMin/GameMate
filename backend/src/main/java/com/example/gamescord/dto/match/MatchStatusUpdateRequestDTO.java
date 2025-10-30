package com.example.gamescord.dto.match;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchStatusUpdateRequestDTO {

    @NotBlank
    private String status;
}

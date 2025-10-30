package com.example.gamescord.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {

    @NotNull(message = "점수는 필수입니다.")
    @Min(value = 1, message = "점수는 1 이상이어야 합니다.")
    @Max(value = 5, message = "점수는 5 이하여야 합니다.")
    private Integer score;

    @Size(max = 255, message = "리뷰는 255자 이하여야 합니다.")
    private String review;
}

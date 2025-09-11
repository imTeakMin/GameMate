package com.example.mvctest.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private Long usersId;
    private String loginId;
    private Long point;
    private String usersName;
    private String usersDescription;
    private LocalDate usersBirthday;
}

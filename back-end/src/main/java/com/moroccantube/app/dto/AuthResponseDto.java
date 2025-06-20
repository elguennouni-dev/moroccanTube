package com.moroccantube.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {

    private String accessToken;
    private String refreshToken;
    private String username;
    private Date accessTokenExpiryDate;

}
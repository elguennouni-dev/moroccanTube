package com.moroccantube.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    private String token;
    private String username;
    private String role;
    private Date expiresAt;
}

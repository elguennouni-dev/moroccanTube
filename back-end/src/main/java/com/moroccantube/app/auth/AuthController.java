package com.moroccantube.app.auth;

import com.moroccantube.app.dao.SignupDao;
import com.moroccantube.app.dao.AuthDao;
import com.moroccantube.app.dto.AuthResponseDto;
import com.moroccantube.app.dto.UserDto;
import com.moroccantube.app.model.RefreshToken;
import com.moroccantube.app.model.User;
import com.moroccantube.app.security.JwtUtil;
import com.moroccantube.app.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthDao authDao) {
        AuthResponseDto authResponseDto = authService.login(authDao);
        return ResponseEntity.ok(authResponseDto);
    }


    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Refresh token is missing or malformed."));
        }

        String refreshTokenString = authHeader.substring(7);

        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.verifyRefreshToken(refreshTokenString);

        if (refreshTokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired refresh token."));
        }

        RefreshToken oldRefreshToken = refreshTokenOptional.get();
        User user = oldRefreshToken.getUser();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .build();

        refreshTokenService.revokeToken(refreshTokenString);

        String newAccessToken = jwtUtil.generateToken(userDetails);
        String newRefreshToken = refreshTokenService.createRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("refreshToken", newRefreshToken);

        return ResponseEntity.ok(tokens);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Refresh token is missing or invalid"));
        }
        String refreshTokenStr = authHeader.substring(7);
        refreshTokenService.revokeToken(refreshTokenStr);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Valid @RequestBody SignupDao request) {
        UserDto createdUserDto = authService.signup(request);
        return new ResponseEntity<>(createdUserDto, HttpStatus.CREATED);
    }



}
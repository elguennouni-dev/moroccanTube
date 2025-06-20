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
    public ResponseEntity<?> login(@Valid @RequestBody AuthDao authDao) {
        return authService.hanldeLogin(authDao);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return authService.handleLogout(request);
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request) {
        return authService.handleRefreshToken(request);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupDao request) {
        return authService.signup(request);
    }


}
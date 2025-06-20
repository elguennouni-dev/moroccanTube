package com.moroccantube.app.auth;

import com.moroccantube.app.dao.AuthDao;
import com.moroccantube.app.dao.SignupDao;
import com.moroccantube.app.dto.AuthResponseDto;
import com.moroccantube.app.dto.UserDto;
import com.moroccantube.app.exception.custom.DuplicateResourceException;
import com.moroccantube.app.exception.custom.InvalidInputException;
import com.moroccantube.app.mapper.UserMapper;
import com.moroccantube.app.model.RefreshToken;
import com.moroccantube.app.model.User;
import com.moroccantube.app.repository.UserRepository;
import com.moroccantube.app.security.JwtUtil;
import com.moroccantube.app.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;


    public ResponseEntity<?> hanldeLogin(AuthDao authDao) {
        try {
            Authentication auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDao.getUsername(), authDao.getPassword())
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String accessToken = jwtUtil.generateToken(userDetails);
            Date accessTokenExpiryDate = jwtUtil.extractExpiration(accessToken);

            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String refreshToken = refreshTokenService.createRefreshToken(user);

            AuthResponseDto authResponseDto = new AuthResponseDto(
                    accessToken,
                    refreshToken,
                    userDetails.getUsername(),
                    accessTokenExpiryDate
            );

            return ResponseEntity.ok(authResponseDto);

        } catch (BadCredentialsException ex) {
            throw new InvalidInputException("Invalid username or password.");
        }
    }


    public ResponseEntity<?> handleLogout(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message","Refresh token is missing or malformed"));
        }
        String refreshTokenString = authHeader.substring(7);
        refreshTokenService.revokeToken(refreshTokenString);
        return ResponseEntity.ok(Map.of("messafe","Logged out successfully"));
    }


    public ResponseEntity<?> signup(SignupDao request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' already exists.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered.");
        }

        User userToSave = new User();
        userToSave.setFullName(request.getFullName());
        userToSave.setUsername(request.getUsername());
        userToSave.setEmail(request.getEmail());
        userToSave.setPassword(passwordEncoder.encode(request.getPassword()));
        userToSave.setEnabled(true);
        User createdUser = userRepository.save(userToSave);
        UserDto userDto = userMapper.toDto(createdUser);

        return ResponseEntity.ok(userDto);
    }


    public ResponseEntity<?> handleRefreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message","Refresh token is missing or malformed"));
        }

        String refreshTokenString = authHeader.substring(7);

        Optional<RefreshToken> refreshTokenOptional = refreshTokenService.verifyRefreshToken(refreshTokenString);

        if(refreshTokenOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message","Invalid or expired refresh token"));
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
        tokens.put("refreshToken",newRefreshToken);

        return ResponseEntity.ok(tokens);
    }

}

package com.moroccantube.app.service;

import com.moroccantube.app.exception.security.RefreshTokenAlreadyRevokedException;
import com.moroccantube.app.exception.security.RefreshTokenNotFoundException;
import com.moroccantube.app.model.RefreshToken;
import com.moroccantube.app.model.User;
import com.moroccantube.app.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.expiration.refresh-token-ms}")
    private long REFRESH_TOKEN_MS;

    @Autowired
    private RefreshTokenRepository tokenRepository;

    public String createRefreshToken(User user) {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plus(Duration.ofMillis(REFRESH_TOKEN_MS));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(uuid);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setRevoked(false);
        tokenRepository.save(refreshToken);

        return uuid;
    }

    public Optional<RefreshToken> verifyRefreshToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(refreshToken -> !refreshToken.isRevoked())
                .filter(refreshToken -> !refreshToken.getExpiryDate().isBefore(LocalDateTime.now()));
    }

    public void revokeToken(String token) {
        RefreshToken refreshToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenNotFoundException("Token Not Found"));
        refreshToken.setRevoked(true);
        tokenRepository.save(refreshToken);
    }

    public void deleteByUser(User user) {
        tokenRepository.deleteAllByUser(user);
    }

}

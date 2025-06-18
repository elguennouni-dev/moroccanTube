package com.moroccantube.app.repository;

import com.moroccantube.app.model.RefreshToken;
import com.moroccantube.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);

    void deleteAllByUser(User user);

//    List<RefreshToken> findAllByUser(User user);
//    void deleteAll(Iterable<? extends RefreshToken> tokens);
}

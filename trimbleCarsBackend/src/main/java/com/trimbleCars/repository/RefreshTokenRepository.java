package com.trimbleCars.repository;

import com.trimbleCars.model.RefreshToken;
import com.trimbleCars.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    List<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByToken(String token);
}

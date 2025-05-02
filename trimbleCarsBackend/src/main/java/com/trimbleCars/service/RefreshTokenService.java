package com.trimbleCars.service;

import com.trimbleCars.exception.CustomException;
import com.trimbleCars.exception.RefreshTokenException;
import com.trimbleCars.model.RefreshToken;
import com.trimbleCars.model.User;
import com.trimbleCars.repository.RefreshTokenRepository;
import com.trimbleCars.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional("authTransactionManager")
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${trimble.app.RefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;


    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());


        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {

            logger.warn("Refresh token expired: {}. Expiry date: {}", token.getToken(), token.getExpiryDate());

            try {
                refreshTokenRepository.delete(token);
                logger.info("Expired refresh token deleted successfully: {}", token.getToken());
            } catch (Exception e) {
                logger.error("Failed to delete expired refresh token: {}. Error: {}", token.getToken(), e.getMessage(), e);
            }

            throw new RefreshTokenException(token.getToken(),
                    "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void delete(User user) {
        List<RefreshToken> refreshToken = refreshTokenRepository.findByUser(user);
        refreshTokenRepository.deleteAll(refreshToken);
    }

}

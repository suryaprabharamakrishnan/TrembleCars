package com.trimbleCars.controller;

import com.trimbleCars.dto.*;
import com.trimbleCars.exception.CustomException;
import com.trimbleCars.model.RefreshToken;
import com.trimbleCars.model.User;
import com.trimbleCars.repository.UserRepository;
import com.trimbleCars.security.JwtUtils;
import com.trimbleCars.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> signInUser(SignInRequest signinRequest) {

        User user = userRepository.findByEmail(signinRequest.getUserName()).orElseThrow(() -> new CustomException("Account not found", HttpStatus.FORBIDDEN));

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getUserName(), signinRequest.getPassword()));
        } catch (Exception e) {
            System.out.println(e);
            throw new CustomException("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String roles = userDetails.getAuthorities().toString();

        AuthResponse authResponse = new AuthResponse();

        authResponse.setToken(jwt);
        authResponse.setRefreshToken(refreshToken.getToken());
        authResponse.setUserId(user.getId());
        authResponse.setUserName(user.getEmail());
        authResponse.setName(user.getName());
        authResponse.setRoles(roles);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signOut")
    public ResponseEntity<Void> signoutUser(SignOutRequest signoutRequest) {

        Optional<User> user = userRepository.findByEmail(signoutRequest.getUserName());

        if (user.isPresent() && user.get().getId() > 0) {
            refreshTokenService.delete(user.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/verifyRefreshToken")
    public ResponseEntity<RefreshTokenResponse> verifyRefreshToken(RefreshTokenRequest refreshTokenRequest) {

        return refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken()).map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser).map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getEmail());
                    RefreshTokenResponse response = new RefreshTokenResponse();
                    response.setAccessToken(token);
                    response.setRefreshToken(refreshTokenRequest.getRefreshToken());
                    return ResponseEntity.ok(response);
                }).orElseThrow(() -> new CustomException("Refresh token was expired. Please make a new signin request!", HttpStatus.FORBIDDEN));
    }

}

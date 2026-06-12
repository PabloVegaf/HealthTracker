package com.healthtracker.backend.service;

import com.healthtracker.backend.dto.AuthResponse;
import com.healthtracker.backend.dto.ChangePasswordRequest;
import com.healthtracker.backend.dto.LoginRequest;
import com.healthtracker.backend.dto.RegisterRequest;
import com.healthtracker.backend.dto.UserResponse;
import com.healthtracker.backend.exception.EmailAlreadyRegisteredException;
import com.healthtracker.backend.exception.InvalidCredentialsException;
import com.healthtracker.backend.model.RefreshToken;
import com.healthtracker.backend.model.User;
import com.healthtracker.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException();
        }

        User user = new User();
        user.setEmail(email);
        user.setName(request.name().trim());
        // BCrypt incluye salt interno, por eso nunca guardamos la password plana.
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        return toUserResponse(userRepository.save(user));
    }

    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return buildAuthResponse(user, refreshTokenService.createRefreshToken(user));
    }

    public AuthResponse refresh(String refreshTokenValue) {
        RefreshToken refreshToken = refreshTokenService.rotateRefreshToken(refreshTokenValue);
        return buildAuthResponse(refreshToken.getUser(), refreshToken);
    }

    public void logout(String refreshTokenValue) {
        refreshTokenService.deleteRefreshToken(refreshTokenValue);
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(normalizeEmail(email))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
        // Al cambiar password, se cierran sesiones largas abiertas con refresh token.
        refreshTokenService.deleteAllUserRefreshTokens(user.getId());
    }

    private AuthResponse buildAuthResponse(User user, RefreshToken refreshToken) {
        String accessToken = jwtService.generateAccessToken(user);
        return new AuthResponse(accessToken, refreshToken.getToken(), jwtService.getAccessTokenExpirationSeconds());
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}

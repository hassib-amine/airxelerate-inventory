package com.airxelerate.inventory.service.user;

import com.airxelerate.inventory.persistence.entity.user.User;
import com.airxelerate.inventory.persistence.repository.user.UserJpaRepository;
import com.airxelerate.inventory.security.JwtTokenProvider;
import com.airxelerate.inventory.usecase.request.user.LoginRequest;
import com.airxelerate.inventory.usecase.response.user.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication Service Implementation
 * Handles user authentication and JWT token generation
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserJpaRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.username(),
                            loginRequest.password()
                    )
            );

            String token = tokenProvider.generateToken(authentication);
            User user = userRepository.findByUsername(loginRequest.username())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + loginRequest.username()));

            return new AuthResponse(token, loginRequest.username(), user.getRole().name());
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}


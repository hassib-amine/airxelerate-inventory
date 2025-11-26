package com.airxelerate.inventory.service.user;

import com.airxelerate.inventory.persistence.entity.user.User;
import com.airxelerate.inventory.persistence.repository.user.UserJpaRepository;
import com.airxelerate.inventory.security.JwtTokenProvider;
import com.airxelerate.inventory.usecase.request.user.LoginRequest;
import com.airxelerate.inventory.usecase.response.user.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserJpaRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse authenticate(LoginRequest loginRequest) {
        String username = loginRequest.username();
        log.info("Authentication attempt for username='{}'", username);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username,
                            loginRequest.password()
                    )
            );

            log.debug("Authentication successful for username='{}'", username);

            String token = tokenProvider.generateToken(authentication);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        log.error("User entity not found after successful authentication, username='{}'", username);
                        return new UsernameNotFoundException("User not found: " + username);
                    });

            log.info("JWT token generated for username='{}', role='{}'", username, user.getRole().name());

            return new AuthResponse(token, username, user.getRole().name());
        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for username='{}': invalid credentials", username);
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}


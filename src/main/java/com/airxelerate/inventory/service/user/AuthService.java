package com.airxelerate.inventory.service.user;

import com.airxelerate.inventory.usecase.request.user.LoginRequest;
import com.airxelerate.inventory.usecase.response.user.AuthResponse;

/**
 * Authentication Service Interface
 * Defines contract for user authentication operations
 */
public interface AuthService {
    /**
     * Authenticates a user and returns a JWT token
     *
     * @param loginRequest the login credentials
     * @return AuthResponse containing JWT token and user information
     */
    AuthResponse authenticate(LoginRequest loginRequest);
}

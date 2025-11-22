package com.airxelerate.inventory.controller.auth;

import com.airxelerate.inventory.service.user.AuthService;
import com.airxelerate.inventory.usecase.request.user.LoginRequest;
import com.airxelerate.inventory.usecase.response.user.AuthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * Handles authentication endpoints
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Returns JWT token after successful authentication")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse response = authService.authenticate(loginRequest);
        return ResponseEntity.ok(response);
    }
}


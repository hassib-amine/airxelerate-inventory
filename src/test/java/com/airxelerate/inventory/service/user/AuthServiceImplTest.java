package com.airxelerate.inventory.service.user;

import com.airxelerate.inventory.persistence.entity.user.Role;
import com.airxelerate.inventory.persistence.entity.user.User;
import com.airxelerate.inventory.persistence.repository.user.UserJpaRepository;
import com.airxelerate.inventory.security.JwtTokenProvider;
import com.airxelerate.inventory.usecase.request.user.LoginRequest;
import com.airxelerate.inventory.usecase.response.user.AuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private Authentication authentication;

    @Test
    void authenticate_whenValidCredentials_shouldReturnAuthResponse() {
        // given
        LoginRequest request = new LoginRequest("john", "password");
        User user = User.builder()
                .id(1L)
                .username("john")
                .password("encoded")
                .role(Role.ROLE_ADMIN)
                .enabled(true)
                .build();

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(tokenProvider.generateToken(authentication)).willReturn("jwt-token");
        given(userRepository.findByUsername("john")).willReturn(Optional.of(user));

        // when
        AuthResponse response = authService.authenticate(request);

        // then
        assertThat(response.token()).isEqualTo("jwt-token");
        assertThat(response.username()).isEqualTo("john");
        assertThat(response.role()).isEqualTo(Role.ROLE_ADMIN.name());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider).generateToken(authentication);
        verify(userRepository).findByUsername("john");
    }

    @Test
    void authenticate_whenUserEntityMissing_shouldThrowUsernameNotFoundException() {
        // given
        LoginRequest request = new LoginRequest("missing", "password");

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);
        given(tokenProvider.generateToken(authentication)).willReturn("jwt-token");
        given(userRepository.findByUsername("missing")).willReturn(Optional.empty());

        // when / then
        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(request));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername("missing");
    }

    @Test
    void authenticate_whenBadCredentials_shouldThrowBadCredentialsException() {
        // given
        LoginRequest request = new LoginRequest("john", "wrong");

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willThrow(new BadCredentialsException("Invalid username or password"));

        // when / then
        assertThrows(BadCredentialsException.class, () -> authService.authenticate(request));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}



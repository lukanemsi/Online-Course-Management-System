package ge.OCMS.service;

import ge.OCMS.configuration.jwt.JwtService;
import ge.OCMS.entity.User;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.exception.custom.InvalidRequestException;
import ge.OCMS.repository.UserRepository;
import ge.OCMS.wrapper.AuthenticationResponse;
import ge.OCMS.wrapper.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPassword");
        userDTO.setRoles(Collections.emptySet());

        user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
    }

    @Test
    public void registerUser_UserAlreadyExists_ThrowsInvalidRequestException() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));

        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                authenticationService.registerUser(userDTO));
        assertEquals("User is Already registered", exception.getMessage());
        verify(userRepository).findByUsername(userDTO.getUsername());
    }

    @Test
    public void registerUser_Success_CreatesUser() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<Void> response = authenticationService.registerUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userRepository).findByUsername(userDTO.getUsername());
        verify(passwordEncoder).encode(userDTO.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void authenticateUser_Success_ReturnsAuthResponse() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));
        when(jwtService.generateJwtToken(any(User.class))).thenReturn("jwtToken");

        ResponseEntity<AuthenticationResponse> response = authenticationService.authenticateUser(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("jwtToken", response.getBody().getJwtToken());
        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateJwtToken(user);
    }

    @Test
    public void authenticateUser_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authenticationService.authenticateUser(userDTO));
    }
}

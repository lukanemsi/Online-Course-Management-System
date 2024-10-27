package ge.OCMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.OCMS.configuration.jwt.JwtService;
import ge.OCMS.service.AuthenticationService;
import ge.OCMS.wrapper.AuthenticationResponse;
import ge.OCMS.wrapper.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    private UserDTO userDTO;
    private AuthenticationResponse authResponse;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("user");
        userDTO.setPassword("password123");

        authResponse = AuthenticationResponse.builder()
                .jwtToken("sample.jwt.token").build();
    }

    @Test
    @WithMockUser
    public void registerUser_ReturnsStatus201() throws Exception {
        Mockito.when(authenticationService.registerUser(any(UserDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated());

        verify(authenticationService).registerUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser
    public void authenticateUser_ReturnsAuthenticationResponseAndStatus200() throws Exception {
        Mockito.when(authenticationService.authenticateUser(any(UserDTO.class)))
                .thenReturn(ResponseEntity.ok(authResponse));

        mockMvc.perform(post("/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)
                        ).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.jwtToken").value(authResponse.getJwtToken()));

        verify(authenticationService).authenticateUser(any(UserDTO.class));
    }
}

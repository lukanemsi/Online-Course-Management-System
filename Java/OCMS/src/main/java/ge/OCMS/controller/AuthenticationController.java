package ge.OCMS.controller;

import ge.OCMS.service.AuthenticationService;
import ge.OCMS.wrapper.AuthenticationResponse;
import ge.OCMS.wrapper.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserDTO userDTO) {
        return authenticationService.registerUser(userDTO);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody UserDTO userDTO) {
        return authenticationService.authenticateUser(userDTO);
    }

}
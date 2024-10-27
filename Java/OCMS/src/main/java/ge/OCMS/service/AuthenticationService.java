package ge.OCMS.service;

import ge.OCMS.configuration.jwt.JwtService;
import ge.OCMS.entity.Role;
import ge.OCMS.entity.User;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.exception.custom.InvalidRequestException;
import ge.OCMS.repository.RoleRepository;
import ge.OCMS.repository.UserRepository;
import ge.OCMS.util.JsonConverter;
import ge.OCMS.util.UserMaskingUtil;
import ge.OCMS.wrapper.AuthenticationResponse;
import ge.OCMS.wrapper.dto.UserDTO;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Log4j2
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Transactional
    public ResponseEntity<Void> registerUser(UserDTO userDTO) {
        if (userRepository.findByUsername(userDTO.getUsername()).isPresent())
            throw new InvalidRequestException("User is Already registered");

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Set<Role> roleSet = new HashSet<>();
        userDTO.getRoles().forEach(role -> roleRepository.findByRoleName(role.getName()).ifPresent(roleSet::add));
        user.setRoles(roleSet);
        userRepository.save(user);
        UserDTO maskedDTO = UserMaskingUtil.maskUser(userDTO);
        log.debug("User registered: {}", JsonConverter.toJson(maskedDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity<AuthenticationResponse> authenticateUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        var jwtToken = jwtService.generateJwtToken((userRepository.findByUsername(user.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User Not Found")
        )));
        UserDTO maskedDTO = UserMaskingUtil.maskUser(userDTO);
        log.debug("User authenticated: {}", JsonConverter.toJson(maskedDTO));
        return new ResponseEntity<>(AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build(), HttpStatus.OK);
    }
}
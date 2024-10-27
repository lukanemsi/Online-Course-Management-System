package ge.OCMS.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class AuthenticatedUserUtilTest {

    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAuthenticatedUser_whenUserIsAuthenticated_thenReturnsUserDetails() {
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<UserDetails> result = AuthenticatedUserUtil.getAuthenticatedUser();

        assertTrue(result.isPresent());
        assertEquals(userDetails, result.get());
    }

    @Test
    void getAuthenticatedUser_whenUserIsNotAuthenticated_thenReturnsEmpty() {
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.isAuthenticated()).thenReturn(false);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<UserDetails> result = AuthenticatedUserUtil.getAuthenticatedUser();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAuthenticatedUser_whenAuthenticationIsNull_thenReturnsEmpty() {
        Mockito.when(securityContext.getAuthentication()).thenReturn(null);

        Optional<UserDetails> result = AuthenticatedUserUtil.getAuthenticatedUser();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAuthenticatedUser_whenPrincipalIsNotUserDetails_thenReturnsEmpty() {
        Authentication authentication = Mockito.mock(Authentication.class);

        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getPrincipal()).thenReturn("notUserDetails");
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        Optional<UserDetails> result = AuthenticatedUserUtil.getAuthenticatedUser();

        assertTrue(result.isEmpty());
    }
}

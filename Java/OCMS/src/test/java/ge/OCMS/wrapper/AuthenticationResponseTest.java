package ge.OCMS.wrapper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthenticationResponseTest {

    @Test
    void build_whenGivenJwtToken_thenCreatesAuthenticationResponse() {
        String jwtToken = "sample.jwt.token";

        AuthenticationResponse response = AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();

        assertNotNull(response);
        assertEquals(jwtToken, response.getJwtToken());
    }

    @Test
    void setJwtToken_whenCalled_thenUpdatesJwtToken() {
        AuthenticationResponse response = AuthenticationResponse.builder().build();
        String jwtToken = "new.jwt.token";
        response.setJwtToken(jwtToken);

        assertEquals(jwtToken, response.getJwtToken());
    }

    @Test
    void toString_whenCalled_thenReturnsFormattedString() {
        String jwtToken = "sample.jwt.token";
        AuthenticationResponse response = AuthenticationResponse.builder()
                .jwtToken(jwtToken)
                .build();

        String expectedString = "AuthenticationResponse(jwtToken=" + jwtToken + ")";
        assertEquals(expectedString, response.toString());
    }
}

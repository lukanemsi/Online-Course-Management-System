package ge.OCMS.exception;

import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.exception.custom.InvalidRequestException;
import ge.OCMS.wrapper.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = Mockito.mock(WebRequest.class);
    }

    @Test
    void handleEntityNotFoundException_returnsNotFoundResponse() {
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");
        when(webRequest.getDescription(false)).thenReturn("Requested entity not found");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleEntityNotFoundException(exception, webRequest);

        ErrorResponse errorResponse = responseEntity.getBody();

        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Entity not found", errorResponse.getError());
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), errorResponse.getMessage());
        assertEquals("Requested entity not found", errorResponse.getPath());
    }

    @Test
    void handleInvalidRequestException_returnsBadRequestResponse() {
        InvalidRequestException exception = new InvalidRequestException("Invalid request");
        when(webRequest.getDescription(false)).thenReturn("Bad request description");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleInvalidRequestException(exception, webRequest);

        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Invalid request", errorResponse.getError());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), errorResponse.getMessage());
        assertEquals("Bad request description", errorResponse.getPath());
    }

    @Test
    void handleGlobalException_returnsInternalServerErrorResponse() {
        Exception exception = new Exception("Unexpected error occurred");
        when(webRequest.getDescription(false)).thenReturn("Global exception occurred");

        ResponseEntity<ErrorResponse> responseEntity = globalExceptionHandler.handleGlobalException(exception, webRequest);

        ErrorResponse errorResponse = responseEntity.getBody();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Unexpected error occurred", errorResponse.getError());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), errorResponse.getMessage());
        assertEquals("Global exception occurred", errorResponse.getPath());
    }
}

package ge.OCMS.wrapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    @BeforeEach
    void setUp() {
        timestamp = LocalDateTime.now();
        status = 404;
        error = "Not Found";
        message = "The requested resource was not found";
        path = "/api/resource";
    }

    @Test
    void build_whenGivenValidInputs_thenCreatesCorrectErrorResponse() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();

        assertNotNull(errorResponse);
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
    }

    @Test
    void setTimestamp_whenCalled_thenUpdatesTimestamp() {
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        errorResponse.setTimestamp(timestamp);
        assertEquals(timestamp, errorResponse.getTimestamp());
    }

    @Test
    void setStatus_whenCalled_thenUpdatesStatus() {
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        errorResponse.setStatus(status);
        assertEquals(status, errorResponse.getStatus());
    }

    @Test
    void setError_whenCalled_thenUpdatesError() {
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        errorResponse.setError(error);
        assertEquals(error, errorResponse.getError());
    }

    @Test
    void setMessage_whenCalled_thenUpdatesMessage() {
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        errorResponse.setMessage(message);
        assertEquals(message, errorResponse.getMessage());
    }

    @Test
    void setPath_whenCalled_thenUpdatesPath() {
        ErrorResponse errorResponse = ErrorResponse.builder().build();
        errorResponse.setPath(path);
        assertEquals(path, errorResponse.getPath());
    }

    @Test
    void equals_whenAllFieldsAreEqual_thenReturnsTrue() {
        ErrorResponse errorResponse1 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();

        ErrorResponse errorResponse2 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();

        assertEquals(errorResponse1, errorResponse2);
        assertEquals(errorResponse1.hashCode(), errorResponse2.hashCode());
    }

    @Test
    void equals_whenFieldsAreDifferent_thenReturnsFalse() {
        ErrorResponse errorResponse1 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();

        ErrorResponse errorResponse2 = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(500)
                .error(error)
                .message(message)
                .path(path)
                .build();

        assertNotEquals(errorResponse1, errorResponse2);
    }

    @Test
    void toString_whenCalled_thenReturnsFormattedString() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(timestamp)
                .status(status)
                .error(error)
                .message(message)
                .path(path)
                .build();

        String expectedString = "ErrorResponse(timestamp=" + timestamp +
                ", status=" + status +
                ", error=" + error +
                ", message=" + message +
                ", path=" + path + ")";
        assertEquals(expectedString, errorResponse.toString());
    }
}

package ge.OCMS.util;

import ge.OCMS.wrapper.ErrorResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JsonConverterTest {

    @Test
    void toJson_whenGivenNull_thenReturnsStringNull() {
        String jsonResult = JsonConverter.toJson(null);

        assertEquals("null", jsonResult);
    }

    @Test
    void toJson_whenGivenValidObject_thenReturnsJsonString() {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.of(2024, 10, 27, 10, 0))
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .path("/some/path")
                .build();

        String jsonResult = JsonConverter.toJson(errorResponse);

        String expectedJson = "{\"timestamp\":[2024,10,27,10,0],\"status\":404,\"error\":\"Not Found\",\"message\":\"Resource not found\",\"path\":\"/some/path\"}";
        assertEquals(expectedJson, jsonResult);
    }

    @Test
    void toJson_whenConversionFails_thenReturnsToString() {
        Object invalidObject = new Object() {
            @Override
            public String toString() {
                return "invalidObjectToString";
            }
        };

        String jsonResult = JsonConverter.toJson(invalidObject);

        assertEquals("invalidObjectToString", jsonResult);
    }

}

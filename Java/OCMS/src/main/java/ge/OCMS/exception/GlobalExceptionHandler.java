package ge.OCMS.exception;

import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.exception.custom.InvalidRequestException;
import ge.OCMS.util.JsonConverter;
import ge.OCMS.wrapper.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = generateErrorResponse(
                httpStatus,
                ex.getMessage(),
                request.getDescription(false));
        log.warn("EntityNotFound Request ErrorResponse: {}", JsonConverter.toJson(errorResponse));
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = generateErrorResponse(
                httpStatus,
                ex.getMessage(),
                request.getDescription(false));
        log.warn("Invalid Request ErrorResponse: {}", JsonConverter.toJson(errorResponse));
        return new ResponseEntity<>(errorResponse, httpStatus);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse errorResponse = generateErrorResponse(
                httpStatus,
                ex.getMessage(),
                request.getDescription(false));
        log.error("Unexpected ErrorResponse: {}", JsonConverter.toJson(errorResponse));
        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    private ErrorResponse generateErrorResponse(HttpStatus httpStatus, String errorMessage, String path) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .message(httpStatus.getReasonPhrase())
                .error(errorMessage)
                .path(path)
                .build();
    }
}

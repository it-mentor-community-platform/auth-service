package com.itmentorcommunityplatform.authservice.advice;

import com.itmentorcommunityplatform.authservice.auth.InvalidInitDataException;
import com.itmentorcommunityplatform.authservice.dummyAuth.UserNotFoundException;
import com.itmentorcommunityplatform.authservice.internalUser.InvalidRoleException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidInitDataException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidInitData(InvalidInitDataException ex,
                                                                  HttpServletRequest request) {
        log.warn("Invalid init data: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest httpRequest) {

        log.warn("Response status exception: {}", ex.getReason());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(buildError((HttpStatus) ex.getStatusCode(), ex.getReason(), httpRequest));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingParams(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        String message = "Parameter is missing: " + ex.getParameterName();
        log.warn("Missing request parameter: {}", ex.getParameterName());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message, request));
    }

    @ExceptionHandler(InvalidRoleException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRole(InvalidRoleException ex, HttpServletRequest request) {
        log.warn("Invalid role in request: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, errorMessage, request));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(
            UserNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildError(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        request
                ));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleOtherExceptions(Exception ex,
                                                                  HttpServletRequest request) {
        log.error("Unexpected error: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Internal server error",
                        request));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        String message = "Invalid parameter type";

        log.warn("Invalid parameter type: {}", ex.getParameter().getParameterType());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, message, request));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingRequestHeader(
            MissingRequestHeaderException ex,
            HttpServletRequest request) {

        String message = String.format("Required header '%s' is missing", ex.getHeaderName());

        log.warn("Missing header: {} for URI: {}", ex.getHeaderName(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(buildError(HttpStatus.FORBIDDEN, message, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req){

        log.warn("Bad request: {}", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), req));
    }


    private ErrorResponseDto buildError(HttpStatus status, String message, HttpServletRequest request) {
        return new ErrorResponseDto(
                Instant.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

}

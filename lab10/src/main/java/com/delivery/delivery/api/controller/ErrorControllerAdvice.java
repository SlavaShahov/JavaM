package com.delivery.delivery.api.controller;

import com.delivery.delivery.core.exception.ForbiddenException;
import com.delivery.delivery.core.exception.InvalidInputException;
import com.delivery.delivery.core.exception.MessageException;
import com.delivery.delivery.core.exception.NotFoundException;
import com.delivery.delivery.core.exception.ValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.utils.PropertyResolverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RestControllerAdvice
public class ErrorControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger(ErrorControllerAdvice.class);

    private final PropertyResolverUtils propertyResolverUtils;

    @Autowired
    public ErrorControllerAdvice(PropertyResolverUtils propertyResolverUtils) {
        this.propertyResolverUtils = propertyResolverUtils;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        return handleCustomException(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleInvalidInputExceptionException(InvalidInputException exception) {
        return handleCustomException(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException exception) {
        return handleCustomException(exception, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception) {
        return handleCustomException(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException exception) {
        return handleCustomException(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error(exception.getMessage());
        return handleCodeAbleException(HttpStatus.INTERNAL_SERVER_ERROR, new MessageException(1, message("Internal server error")));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return handleBindValidationException(exception);
    }

    public record ErrorResponse(String errorType, String message, int code) {
    }

    protected ResponseEntity<ErrorResponse> handleCustomException(Exception exception, HttpStatus status) {
        return ResponseEntity.status(status).body(body(exception.getMessage(), status));
    }

    protected ResponseEntity<ErrorResponse> handleCodeAbleException(HttpStatus status, MessageException exception) {
        return ResponseEntity.status(status).body(body(exception.getMessage(), status, exception.getCode()));
    }

    protected ErrorResponse body(String message, HttpStatus status, int code) {
        return new ErrorResponse(status.name(), message, code);
    }

    protected ResponseEntity<ErrorResponse> handleBindValidationException(MethodArgumentNotValidException exception) {
        String message = IntStream.range(0, exception.getErrorCount()).mapToObj(i -> i + 1 + "." + exception.getAllErrors()
                .get(i).getDefaultMessage()).collect(Collectors.joining("; "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body(message, HttpStatus.BAD_REQUEST, 400));
    }

    protected ErrorResponse body(String message, HttpStatus status) {
        return new ErrorResponse(status.name(), message, status.value());
    }

    private String message(String property) {
        return this.propertyResolverUtils.resolve(property, Locale.getDefault());
    }
}

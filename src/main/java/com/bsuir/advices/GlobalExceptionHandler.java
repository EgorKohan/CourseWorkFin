package com.bsuir.advices;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.web.util.UrlUtils.buildFullRequestUrl;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            ResponseStatusException.class,
            InternalAuthenticationServiceException.class
    })
    public ErrorInfo handleCustomException(ResponseStatusException ex, HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(ex.getStatus().value());
        return new ErrorInfo(buildFullRequestUrl(request), ex.getReason());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ValidationErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String[] split = violation.getPropertyPath().toString().split("\\.");
            String field = split[split.length - 1];
            error.getViolations().add(
                    new Violation(field, violation.getMessage()));
        }
        return error;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }
        return ResponseEntity.status(status).body(Optional.of(error));
    }

    @ExceptionHandler(RuntimeException.class)
    public ErrorInfo handleException(HttpServletRequest request, HttpServletResponse response, RuntimeException exception) {
        exception.printStackTrace();
        logger.error("Exception: " + exception.getClass() + "message: " + exception.getMessage());
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ErrorInfo(buildFullRequestUrl(request), "Something went wrong at the server side");
    }

    @Getter
    public static class ErrorInfo {
        private final String url;
        private final String info;

        ErrorInfo(String url, String info) {
            this.url = url;
            this.info = info;
        }
    }

    @Data
    public static class ValidationErrorResponse {

        private List<Violation> violations = new ArrayList<>();

    }

    @Data
    @AllArgsConstructor
    public static class Violation {

        private final String fieldName;
        private final String message;

    }

}

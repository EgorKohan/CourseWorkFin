package com.bsuir.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class TokenWasExpiredException extends RuntimeException {

    public TokenWasExpiredException() {
        super();
    }

    public TokenWasExpiredException(String message) {
        super(message);
    }
}

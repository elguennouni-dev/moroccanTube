package com.moroccantube.app.exception.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RefreshTokenRevokedException extends RuntimeException{

    public RefreshTokenRevokedException(String message) {
        super(message);
    }

    public RefreshTokenRevokedException(String message, Throwable cause) {
        super(message, cause);
    }

}

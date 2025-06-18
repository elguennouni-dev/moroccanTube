package com.moroccantube.app.exception.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class RefreshTokenAlreadyRevokedException extends RuntimeException{

    public RefreshTokenAlreadyRevokedException(String message) {
        super(message);
    }

    public RefreshTokenAlreadyRevokedException(String message, Throwable cause) {
        super(message, cause);
    }

}

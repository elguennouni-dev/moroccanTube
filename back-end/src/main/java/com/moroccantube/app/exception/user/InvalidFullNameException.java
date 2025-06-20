package com.moroccantube.app.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFullNameException extends RuntimeException {

    public InvalidFullNameException() {
        super("Full name is not valid");
    }

    public InvalidFullNameException(String message) {
        super(message);
    }

    public InvalidFullNameException(String message, Throwable cause) {
        super(message, cause);
    }
}

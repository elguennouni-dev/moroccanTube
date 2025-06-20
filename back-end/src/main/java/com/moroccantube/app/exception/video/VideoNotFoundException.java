package com.moroccantube.app.exception.video;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VideoNotFoundException extends RuntimeException {

    public VideoNotFoundException(String message) {
        super(message);
    }

    public VideoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}

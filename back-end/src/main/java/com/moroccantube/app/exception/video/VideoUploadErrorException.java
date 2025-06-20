package com.moroccantube.app.exception.video;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VideoUploadErrorException extends RuntimeException{

    public VideoUploadErrorException(String message) {
        super(message);
    }

    public VideoUploadErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}

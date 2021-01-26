package com.training.schedule.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(final String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, boolean integration) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

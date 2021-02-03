package com.training.schedule.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Session Already Closed")
public class SessionClosedException extends RuntimeException {

    public SessionClosedException(final String message) {
        super(message);
    }
}

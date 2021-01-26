package com.training.schedule.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Session Already Closed")
public class SessionAlreadyClosedException extends RuntimeException {

    public SessionAlreadyClosedException(final String message) {
        super(message);
    }
}

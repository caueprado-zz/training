package com.training.schedule.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = BAD_REQUEST, reason = "Person not able")
public class PersonNotAbleToVoteException extends RuntimeException {

    public PersonNotAbleToVoteException() {
    }

    public PersonNotAbleToVoteException(String message) {
        super(message);
    }
}

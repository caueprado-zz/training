package com.training.schedule.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Person already created")
public class PersonAlreadyCreatedException extends RuntimeException {

    public PersonAlreadyCreatedException(String document_already_exists) {
    }
}

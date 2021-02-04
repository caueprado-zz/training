package com.training.schedule.controller.validator;

import static java.util.Objects.isNull;

import org.springframework.stereotype.Component;

import com.training.schedule.controller.request.PersonRequest;
import com.training.schedule.domain.exception.BadRequestException;

@Component
public final class PersonCreationValidator {

    public void validate(PersonRequest personRequest) {
        if (isNull(personRequest.getDocument()) || personRequest.getDocument().isBlank() || isNull(personRequest.getName())) {
            throw new BadRequestException("Obligatory field is missing");
        }
        if (personRequest.getDocument().length() != 14) {
            throw new BadRequestException(String.format("Invalid document size %d, length should be 14", personRequest.getDocument().length()));
        }
    }

}

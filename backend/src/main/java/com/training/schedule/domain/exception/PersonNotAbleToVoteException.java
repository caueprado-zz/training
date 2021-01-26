package com.training.schedule.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Person not able")
public class PersonNotAbleToVoteException extends RuntimeException {

}

package com.training.schedule.controller;

import com.training.schedule.domain.exception.*;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(final BadRequestException ex, WebRequest request) {
        val body = createResponseBody("Field is missing");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonAlreadyCreatedException.class)
    public ResponseEntity<Object> handlePersonAlreadyException(PersonAlreadyCreatedException ex, WebRequest request) {
        val body = createResponseBody("Person Already Created");
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SessionClosedException.class)
    public ResponseEntity<Object> handlePersonAlreadyException(SessionClosedException ex, WebRequest request) {
        val body = createResponseBody("Session cannot be open again");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<Object> handleSessionNotFound(final SessionNotFoundException ex, final WebRequest request) {
        val body = createResponseBody("Session cannot be open again");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException ex, final WebRequest request) {
        val body = createResponseBody("Illegal argument exception");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonNotAbleToVoteException.class)
    public ResponseEntity<Object> handlePersonNotAbleToVoteException(final PersonNotAbleToVoteException ex, final WebRequest request) {
        val body = createResponseBody("Person already voted");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    protected Map<String, Object> createResponseBody(final String s) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", s);
        return body;
    }
}

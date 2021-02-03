package com.training.schedule.domain.exception;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Schedule not found")
public class ScheduleNotFoundException extends RuntimeException {

    public ScheduleNotFoundException(final String message) {
        super(message);
    }
}

package com.training.schedule.controller.mapper;

import com.training.schedule.controller.response.SessionResponse;
import com.training.schedule.domain.schedule.Schedule;
import com.training.schedule.domain.schedule.session.Session;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public final class SessionMapper {

    public static SessionResponse toResponse(final Schedule schedule) {
        val session = schedule.getSession();
        return SessionResponse.builder()
            .state(session.getState())
            .startTime(session.getStartTime())
            .endTime(session.getEndTime())
            .build();
    }
}

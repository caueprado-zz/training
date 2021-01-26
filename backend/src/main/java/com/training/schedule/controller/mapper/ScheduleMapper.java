package com.training.schedule.controller.mapper;

import com.training.schedule.controller.request.ScheduleRequest;
import com.training.schedule.controller.request.ScheduleResponse;
import com.training.schedule.domain.schedule.Schedule;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ScheduleMapper {

    public static ScheduleResponse toResponse(final Schedule schedule) {
        return ScheduleResponse.builder()
            .name(schedule.getName())
            .category(schedule.getCategory())
            .description(schedule.getDescription())
            .date(schedule.getDate())
            .session(schedule.getSession())
            .build();
    }

    public static Schedule toModel(final ScheduleRequest scheduleRequest) {
        return Schedule.builder()
            .name(scheduleRequest.getName())
            .category(scheduleRequest.getCategory())
            .description(scheduleRequest.getDescription())
            .session(scheduleRequest.getSession())
            .build();
    }

}

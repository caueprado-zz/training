package com.training.schedule.controller;


import com.training.schedule.controller.mapper.ScheduleMapper;
import com.training.schedule.controller.request.ScheduleRequest;
import com.training.schedule.controller.request.ScheduleResponse;
import com.training.schedule.domain.exception.ScheduleNotFoundException;
import com.training.schedule.domain.schedule.Schedule;
import com.training.schedule.service.ScheduleSessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.training.schedule.controller.mapper.ScheduleMapper.toResponse;

@Api("Pautas")
@RestController
@RequestMapping("v1/schedules")
@RequiredArgsConstructor
public class ScheduleSessionController {

    private final ScheduleSessionService scheduleSessionService;

    @GetMapping("/{scheduleId}")
    @ApiOperation(value = "Get a schedule by their id", tags = "Schedule")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = ScheduleResponse.class),
        @ApiResponse(code = 404, message = "Schedule not found", response = ScheduleNotFoundException.class)
    })
    public ScheduleResponse get(@PathVariable("scheduleId") final String scheduleId) {
        return toResponse(scheduleSessionService.findSchedule(scheduleId));
    }

    @GetMapping
    @ApiOperation(value = "Get all created schedules", tags = "Schedule")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = List.class)
    })
    public List<Schedule> list(@RequestParam("page") final int page) {
        return scheduleSessionService.findAllSchedules(page).toList();
    }

    @PostMapping
    @ApiOperation(value = "Create a new schedule", tags = "Schedule")
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK", response = ScheduleResponse.class)
    })
    public ScheduleResponse create(@RequestBody final ScheduleRequest scheduleRequest) {
        return toResponse(scheduleSessionService.createSchedule(ScheduleMapper.toModel(scheduleRequest)));
    }

}

package com.training.schedule.domain.schedule.repository;

import com.training.schedule.domain.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionCustomRepository {

    List<Schedule> findOpenSession(final LocalDateTime localDateTime);

}

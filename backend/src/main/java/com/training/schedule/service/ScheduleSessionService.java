package com.training.schedule.service;

import com.training.schedule.domain.exception.ScheduleNotFoundException;
import com.training.schedule.domain.schedule.Schedule;
import com.training.schedule.domain.schedule.repository.ScheduleRepository;
import com.training.schedule.domain.schedule.session.Session;
import com.training.schedule.domain.schedule.session.SessionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.training.schedule.domain.schedule.session.SessionState.NEW;
import static java.time.LocalDateTime.now;
import static org.springframework.data.domain.Sort.Direction.ASC;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleSessionService {

    private static final int PAGE_SIZE = 10;
    private final ScheduleRepository scheduleRepository;

    public Schedule findSchedule(final String scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(ScheduleNotFoundException::new);
    }

    public Page<Schedule> findAllSchedules(final int page) {
        val pageable = PageRequest.of(page, PAGE_SIZE);
        return scheduleRepository.findAll(pageable);
    }

    public void save(final Schedule schedule) {
        scheduleRepository.save(schedule);
    }

    public Schedule createSchedule(final Schedule schedule) {
        log.info("Creating a new schedule, {}", schedule);
        schedule.setDate(LocalDateTime.now());
        schedule.setSession(buildNewSession());
        return scheduleRepository.save(schedule);
    }

    private Session buildNewSession() {
        return Session.builder()
            .state(NEW)
            .build();
    }

}

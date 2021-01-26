package com.training.schedule.service;

import com.training.schedule.domain.exception.ScheduleNotFoundException;
import com.training.schedule.domain.exception.SessionAlreadyClosedException;
import com.training.schedule.domain.schedule.Schedule;
import com.training.schedule.domain.schedule.repository.ScheduleRepository;
import com.training.schedule.domain.schedule.session.Session;
import com.training.schedule.domain.schedule.session.SessionState;
import com.training.schedule.infra.producer.SessionClosedProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.training.schedule.domain.schedule.session.SessionState.CLOSED;
import static com.training.schedule.domain.schedule.session.SessionState.NEW;
import static java.time.LocalDateTime.now;
import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionService {

    private final ScheduleRepository scheduleRepository;
    private final SessionClosedProducer sessionClosedProducer;

    public Schedule openSession(final String scheduleId, final int sessionDuration) {
        val schedule = findValidSchedule(scheduleId);
        validateSessionState(scheduleId);

        val fixedDuration = isNull(sessionDuration) ? 1 : sessionDuration;

        val session = buildOpenSession(fixedDuration);
        schedule.setSession(session);

        val current = scheduleRepository.save(schedule);

        log.info("Openning session {}, with duration of {}", current.getId(), fixedDuration);

        scheduleSessionClose(scheduleId, fixedDuration);
        return current;
    }

    private Schedule findValidSchedule(String scheduleId) {
        return scheduleRepository.findById(scheduleId)
            .orElseThrow(ScheduleNotFoundException::new);
    }

    private void validateSessionState(String scheduleId) {
        scheduleRepository.findById(scheduleId)
            .map(Schedule::getSession)
            .filter(session -> !NEW.equals(session.getState()))
            .map(session -> new SessionAlreadyClosedException("Session is in a invalid state to open."));
    }

    public void scheduleSessionClose(final String scheduleId, final int sessionDuration) {
        val scheduler = Executors.newSingleThreadScheduledExecutor();
        final Runnable task = () -> closeSession(scheduleId);
        scheduler.schedule(task, sessionDuration, TimeUnit.MINUTES);
        scheduler.shutdown();
    }

    public Schedule closeSession(final String scheduleId) {
        log.info("Closing session {}, at {}", scheduleId, now());
        val currentSchedule = findValidSchedule(scheduleId);
        val session = currentSchedule.getSession();
        if (CLOSED.equals(session.getState())) {
            throw new SessionAlreadyClosedException(String.format("Session %s already closed", scheduleId));
        }
        log.info("close current sesssion {}", scheduleId);
        session.setState(CLOSED);
        sessionClosedProducer.close(scheduleId);
        return scheduleRepository.save(currentSchedule);
    }

    private Session buildOpenSession(final int sessionDuration) {
        return Session.builder()
            .state(SessionState.OPEN)
            .startTime(now())
            .endTime(now().plusMinutes(sessionDuration))
            .build();
    }
}

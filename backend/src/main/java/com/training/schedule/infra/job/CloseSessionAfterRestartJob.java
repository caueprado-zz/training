package com.training.schedule.infra.job;

import com.training.schedule.domain.schedule.Schedule;
import com.training.schedule.domain.schedule.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.training.schedule.domain.schedule.session.SessionState.CLOSED;
import static java.time.LocalDateTime.now;

/**
 * {@link java.util.concurrent.Executor} para encerrar as sessões por terem a duração de um minuto, uma possivel melhoria seria utilizar uma job com
 * quartz, porem para simplicidade da aplicação e evitar over-engineering
 */
@Slf4j
@Component
public class CloseSessionAfterRestartJob {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Scheduled(cron = "0 */1 * * * *")
    public void verify() {
        log.info("Verify sessions to close");
        for (final Schedule schedule : scheduleRepository.findOpenSession(now())) {
            val currentSession = schedule.getSession();
            currentSession.setState(CLOSED);
            scheduleRepository.save(schedule);
            log.info("Job closed session");
        }
    }

}

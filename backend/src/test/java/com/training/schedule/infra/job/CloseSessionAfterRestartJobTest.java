package com.training.schedule.infra.job;


import com.training.schedule.TestBackground;
import com.training.schedule.domain.schedule.repository.ScheduleRepository;
import com.training.schedule.domain.schedule.session.SessionState;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.infra.job.CloseSessionAfterRestartJob;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static com.training.schedule.domain.schedule.session.SessionState.CLOSED;
import static com.training.schedule.domain.schedule.session.SessionState.OPEN;
import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class CloseSessionAfterRestartJobTest extends TestBackground {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private CloseSessionAfterRestartJob closeSessionAfterRestartJob;

    @MockBean
    private DocumentClient documentClient;

    @Test
    public void givenAOpenSession_WithAEndTimeBeforeCurrentTime_ThenTheJobDontChangeSessionState() {
        val expected = buildSchedule(buildSession(OPEN));
        val session = expected.getSession();
        session.setEndTime(LocalDateTime.now().plusMinutes(10));
        scheduleRepository.save(expected);

        assertThat(session.getState()).isEqualTo(OPEN);

        closeSessionAfterRestartJob.verify();
        val result = scheduleRepository.findById(expected.getId()).get();
        assertThat(result.getSession().getState()).isEqualTo(OPEN);
    }

    @Test
    public void givenAOpenSession_WithAEndTimeAfterCurrentTime_ThenTheJobShouldClose() {
        val expected = buildSchedule(buildSession(OPEN));
        val session = expected.getSession();
        session.setEndTime(LocalDateTime.now().minusMinutes(10));
        scheduleRepository.save(expected);

        assertThat(session.getState()).isEqualTo(OPEN);

        closeSessionAfterRestartJob.verify();
        val result = scheduleRepository.findById(expected.getId()).get();
        assertThat(result.getSession().getState()).isEqualTo(CLOSED);
    }
}

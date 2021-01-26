package com.training.schedule.service;

import com.training.schedule.TestBackground;
import com.training.schedule.domain.schedule.repository.ScheduleRepository;
import com.training.schedule.infra.client.DocumentClient;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import static com.training.schedule.domain.schedule.session.SessionState.NEW;

@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleSessionServiceTest extends TestBackground {

    @Autowired
    private ScheduleSessionService sessionService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @MockBean
    private DocumentClient documentClient;

    @Test
    public void givenASchedule_ThenServiceShouldFind() {
        val expected = buildSchedule(buildSession(NEW));
        val result = sessionService.findSchedule(expected.getId());

        assertResult(expected, result);
    }

}

package com.training.schedule.controller;

import com.training.schedule.TestBackground;
import com.training.schedule.controller.request.ScheduleRequest;
import com.training.schedule.infra.client.DocumentClient;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static com.training.schedule.domain.schedule.session.SessionState.NEW;
import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ScheduleSessionControllerTest extends TestBackground {

    @Autowired
    private ScheduleSessionController sessionController;

    @MockBean
    private DocumentClient documentClient;

    @Test
    public void givenAScheduleRequest_shouldCreateASchedule() {
        val schedule = buildScheduleRequest();
        val result = sessionController.create(schedule);
        assertThat(result.getName()).isEqualTo(schedule.getName());
        assertThat(result.getCategory()).isEqualTo(schedule.getCategory());
        assertThat(result.getDescription()).isEqualTo(schedule.getDescription());
    }

    @Test
    public void givenASchedule_shouldReturnSchedule() {
        val newSchedule = buildSchedule(buildSession((NEW)));
        val result = sessionController.get(newSchedule.getId());
        assertThat(result.getName()).isEqualTo(newSchedule.getName());
        assertThat(result.getCategory()).isEqualTo(newSchedule.getCategory());
        assertThat(result.getDescription()).isEqualTo(newSchedule.getDescription());
    }

    @Test
    public void givenASchedule_shouldReturnAllSchedules() {
        buildSchedule(buildSession((NEW)));
        buildSchedule(buildSession((NEW)));

        val result = sessionController.getAll(1);
        assertThat(result.size()).isEqualTo(2);
    }
}

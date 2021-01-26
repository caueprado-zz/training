package com.training.schedule.service;

import com.training.schedule.TestBackground;
import com.training.schedule.domain.person.repository.PersonRepository;
import com.training.schedule.domain.schedule.session.SessionState;
import com.training.schedule.domain.schedule.session.Vote;
import com.training.schedule.infra.client.DocumentClient;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static com.training.schedule.controller.request.VoteOption.NO;
import static com.training.schedule.controller.request.VoteOption.YES;
import static org.assertj.core.api.Assertions.assertThat;

@Profile("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionResultServiceTest extends TestBackground {

    @Autowired
    private SessionResultService sessionResultService;

    @Autowired
    private PersonRepository personRepository;

    @MockBean
    private DocumentClient documentClient;

    @Test
    public void shouldGetSessionResult() {
        val expected = buildSchedule(buildSession(SessionState.OPEN));
        val first = personRepository.save(buildPerson("Caue", "123.321.333-21", true));
        val second = personRepository.save(buildPerson("Tester", "333.555.333-42", true));
        val vote1 = Vote.builder().person(first).vote(YES).build();
        val vote2 = Vote.builder().person(second).vote(NO).build();

        List<Vote> votes = new ArrayList<>();
        votes.add(vote1);
        votes.add(vote2);

        val session = expected.getSession();
        session.setVotes(votes);
        scheduleRepository.save(expected);

        val result = sessionResultService.getSessionResult(expected.getId());
        assertThat(result.getTotal()).isEqualTo(votes.size());
        assertThat(result.getYes()).isEqualTo(1);
        assertThat(result.getNo()).isEqualTo(1);
    }

}

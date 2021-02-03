package com.training.schedule;

import com.training.schedule.controller.request.ScheduleRequest;
import com.training.schedule.controller.request.SessionVoteRequest;
import com.training.schedule.domain.person.Person;
import com.training.schedule.domain.person.repository.PersonRepository;
import com.training.schedule.domain.schedule.repository.ScheduleRepository;
import com.training.schedule.domain.schedule.Schedule;
import com.training.schedule.domain.schedule.session.Session;
import com.training.schedule.domain.schedule.session.SessionState;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;

import static com.training.schedule.controller.request.VoteOption.YES;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class TestBackground {

    @Autowired
    protected ScheduleRepository scheduleRepository;

    @Autowired
    protected PersonRepository personRepository;

    protected Schedule buildSchedule(final Session session) {
        val schedule = Schedule.builder()
            .category("Teste")
            .name("Teste 1")
            .session(session)
            .build();
        return scheduleRepository.save(schedule);
    }

    protected Session buildSession(final SessionState state) {
        return Session.builder()
            .state(state)
            .votes(emptyList())
            .build();
    }

    protected ScheduleRequest buildScheduleRequest() {
        return ScheduleRequest.builder()
            .name("Teste 1")
            .category("Category 1")
            .description("Desc")
            .build();
    }

    /**
     * Methods to build person for testing
     * @param state person's state able to vote
     * @return a person
     */
    protected Person buildPersonWithState(final boolean state) {
        val person = Person.builder()
            .name("Spock")
            .document("123.111.442-31")
            .able(state)
            .build();
        return personRepository.save(person);
    }

    protected Person buildPerson(final String name, final String document, final boolean state) {
        val person = Person.builder()
            .name(name)
            .document(document)
            .able(state)
            .build();
        return personRepository.save(person);
    }

    /**
     * Builders for vote testing
     * @param id
     * @return A SessionVoteRequest Object
     */
    protected SessionVoteRequest buildVoteRequest(final String id) {
        return SessionVoteRequest.builder()
            .personId(id)
            .vote(YES)
            .build();
    }

    /**
     * Method used to do assertions on two objects
     * @param expected expected values
     * @param result execution result
     */
    protected void assertResult(final Schedule expected, final Schedule result) {
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getCategory()).isEqualTo(expected.getCategory());
        assertThat(result.getDescription()).isEqualTo(expected.getDescription());

        val expectedSession = expected.getSession();
        val resultSession = result.getSession();
        assertThat(resultSession.getVoteCount()).isEqualTo(expectedSession.getVoteCount());
        assertThat(resultSession.getState()).isEqualTo(expectedSession.getState());
        assertThat(resultSession.getStartTime()).isEqualTo(expectedSession.getStartTime());
        assertThat(resultSession.getEndTime()).isEqualTo(expectedSession.getEndTime());
    }
}

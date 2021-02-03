package com.training.schedule.controller;


import com.training.schedule.TestBackground;
import com.training.schedule.domain.exception.*;
import com.training.schedule.domain.schedule.session.Vote;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.infra.producer.SessionClosedProducer;
import io.restassured.RestAssured;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static com.training.schedule.controller.request.VoteOption.YES;
import static com.training.schedule.domain.schedule.session.SessionState.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@Profile("test")
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {
    "management.port=8091", "management.context-path=/admin"})
public class SessionControllerTest extends TestBackground {

    @LocalServerPort
    private int port;

    @Autowired
    private SessionController sessionController;

    @MockBean
    private SessionClosedProducer sessionClosedProducer;

    @MockBean
    private DocumentClient documentClient;

    @Before
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void givenASchedule_WhenSessionIsOpened_ThenThisStateShouldBeOPEN() {
        int duration = 1;
        val session = buildSession(NEW);
        val schedule = buildSchedule(session);

        val result = sessionController.open(schedule.getId(), duration);

        assertThat(result).isNotNull();
        assertThat(result.getState()).isEqualTo(OPEN);
    }

    @Test
    public void givenAScheduleWithAClosed_WhenTryToOpenSession_Then() {
        int duration = 1;
        val session = buildSession(CLOSED);
        val schedule = buildSchedule(session);

        try {
            sessionController.open(schedule.getId(), duration);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(IllegalStateException.class);
            assertThat(e.getMessage()).isEqualTo("Session is in a invalid state to open.");
        }
    }

    @Test
    public void givenAScheduleWithAAlreadyOppenedSession_WhenSessionIsOpened_Then() {
        int duration = 1;
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);

        try {
            sessionController.open(schedule.getId(), duration);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(IllegalStateException.class);
            assertThat(e.getMessage()).isEqualTo("Session is in a invalid state to open.");
        }
    }


    /**
     * A new session can be canceled, so this cant be voted.
     */
    @Test
    public void givenANewSession_WhenACloseEventStarted_ThenSessionShouldBeClosed() {
        val session = buildSession(NEW);
        val schedule = buildSchedule(session);

        val result = sessionController.close(schedule.getId());

        doNothing().when(sessionClosedProducer).close(schedule.getId());
        assertThat(result.getState()).isEqualTo(CLOSED);
    }

    /**
     * A open session can be closed, so this can't be voted, anymore.
     */
    @Test
    public void givenAOpenSession_WhenACloseEventStarted_ThenSessionShouldBeClosed() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);

        val result = sessionController.close(schedule.getId());

        doNothing().when(sessionClosedProducer).close(schedule.getId());
        assertThat(result.getState()).isEqualTo(CLOSED);
    }

    /**
     * A closed session can't be canceled again.
     */
    @Test
    public void givenAClosedSession_WhenACloseEventStarted_ShouldReturnAnException() {
        val session = buildSession(CLOSED);
        val schedule = buildSchedule(session);
        try {
            sessionController.close(schedule.getId());
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(SessionClosedException.class);
            assertThat(e.getMessage()).isEqualTo(String.format("Session %s already closed", schedule.getId()));
        }
    }

    /**
     * Success
     */
    @Test
    public void givenAOpenedSession_AndAPersonAbleToVote_WhenThisPersonRequestsAVote_ShouldCanVoteWithSuccess() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        val expectedPerson = buildPersonWithState(true);
        val voteRequest = buildVoteRequest(schedule.getId(), expectedPerson.getId());

        assertThat(session.getVoteCount()).isEqualTo(0);

        val result = sessionController.vote(voteRequest);

        assertThat(result.getVote()).isEqualTo(YES);
    }

    /**
     * Throws an exception and vote is not inserted
     */
    @Test
    public void givenAOpenedSession_AndAPersonAlreadyVoted_WhenThisPersonRequestsAVote_ShouldCantVote() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);

        val expectedPerson = buildPersonWithState(false);

        val voteRequest = buildVoteRequest(schedule.getId(), expectedPerson.getId());

        List<Vote> votes = Collections.singletonList(Vote.builder().person(expectedPerson).vote(YES).build());
        session.setVotes(votes);

        assertThat(session.getVoteCount()).isEqualTo(1);
        try {
            sessionController.vote(voteRequest);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(PersonNotAbleToVoteException.class);
        }
        assertThat(session.getVoteCount()).isEqualTo(1);
    }

    @Test
    public void givenAOpenedSession_AndAPersonUnableToVote_WhenThisPersonRequestsAVote_ShouldCantVoteWithSuccess() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        val newSession = schedule.getSession();

        val expectedPerson = buildPersonWithState(false);

        val voteRequest = buildVoteRequest(schedule.getId(), expectedPerson.getId());

        assertThat(newSession.getVoteCount()).isEqualTo(0);
        try {
            sessionController.vote(voteRequest);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(PersonNotAbleToVoteException.class);
        }
        assertThat(newSession.getVoteCount()).isEqualTo(0);
    }

    @Test
    public void givenAOpenedSession_AndAPersonDoesNotExists_WhenThisPersonRequestsAVote_ShouldReturnAException() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        val newSession = schedule.getSession();

        val voteRequest = buildVoteRequest(schedule.getId(), "132312");

        assertThat(newSession.getVoteCount()).isEqualTo(0);
        try {
            sessionController.vote(voteRequest);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(PersonNotFoundException.class);
        }
        assertThat(newSession.getVoteCount()).isEqualTo(0);
    }

    @Test
    public void givenAClosedSession_AndAPersonAbleToVote_WhenThisPersonRequestsAVote_ShouldCant() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        val newSession = schedule.getSession();

        val expectedPerson = buildPersonWithState(true);

        val voteRequest = buildVoteRequest(schedule.getId(), expectedPerson.getId());

        assertThat(newSession.getVoteCount()).isEqualTo(0);
        try {
            sessionController.vote(voteRequest);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(SessionClosedException.class);
            assertThat(e.getMessage()).isEqualTo(String.format("Session %s, is not open for votes!", schedule.getId()));
        }
        assertThat(newSession.getVoteCount()).isEqualTo(0);
    }

    @Test
    public void givenANewSession_AndAPersonAbleToVote_WhenThisPersonRequestsAVote_ShouldCantVote() {
        val session = buildSession(NEW);
        val schedule = buildSchedule(session);
        val newSession = schedule.getSession();

        val expectedPerson = buildPersonWithState(false);

        val voteRequest = buildVoteRequest(schedule.getId(), expectedPerson.getId());

        assertThat(newSession.getVoteCount()).isEqualTo(0);
        try {
            sessionController.vote(voteRequest);
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(SessionClosedException.class);
            assertThat(e.getMessage()).isEqualTo(String.format("Session %s, is not open for votes!", schedule.getId()));
        }
        assertThat(newSession.getVoteCount()).isEqualTo(0);
    }

    @Test
    public void givenASession_AndRequestsToSessionResult_ShouldReturnSessionResult() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);

        val expectedPerson = buildPersonWithState(true);

        List<Vote> votes = Collections.singletonList(Vote.builder().person(expectedPerson).vote(YES).build());
        session.setVotes(votes);

        scheduleRepository.save(schedule);
        val newSession = schedule.getSession();

        assertThat(newSession.getVoteCount()).isEqualTo(1);
        val result = sessionController.get(schedule.getId());
        assertThat(result.getNo()).isEqualTo(0);
        assertThat(result.getYes()).isEqualTo(1);
        assertThat(result.getTotal()).isEqualTo(1);
    }

    @Test
    public void givenANotExistentSession_AndRequestsToSessionResult_ShouldReturnAException() {
        try {
            sessionController.get("123");
        } catch (Exception e) {
            assertThat(e.getClass()).isEqualTo(ScheduleNotFoundException.class);
        }
    }

}

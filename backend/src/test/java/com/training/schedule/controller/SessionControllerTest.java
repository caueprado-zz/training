package com.training.schedule.controller;


import static com.training.schedule.controller.endpoints.SessionEndpoints.result;
import static com.training.schedule.controller.endpoints.SessionEndpoints.vote;
import static com.training.schedule.controller.request.VoteOption.YES;
import static com.training.schedule.domain.schedule.session.SessionState.CLOSED;
import static com.training.schedule.domain.schedule.session.SessionState.NEW;
import static com.training.schedule.domain.schedule.session.SessionState.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.cloud.contract.spec.internal.HttpStatus.BAD_REQUEST;
import static org.springframework.cloud.contract.spec.internal.HttpStatus.NOT_FOUND;
import static org.springframework.cloud.contract.spec.internal.HttpStatus.OK;

import java.util.Collections;
import java.util.List;

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

import com.training.schedule.TestBackground;
import com.training.schedule.controller.endpoints.SessionEndpoints;
import com.training.schedule.controller.request.SessionResult;
import com.training.schedule.controller.request.SessionVoteRequest;
import com.training.schedule.controller.response.SessionResponse;
import com.training.schedule.domain.schedule.session.Vote;
import com.training.schedule.infra.client.DocumentClient;
import com.training.schedule.infra.producer.SessionClosedProducer;

import io.restassured.RestAssured;
import lombok.val;

/**
 * Black box integration testing for session flows
 */
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
    public void givenAScheduleInNewState_WhenSessionIsOpened_ThenThisStateShouldBeOPEN() {
        int minutes = 1;
        val session = buildSession(NEW);
        val schedule = buildSchedule(session);

        val result = SessionEndpoints.open(schedule.getId(), minutes)
                .statusCode(OK)
                .body("state", equalTo("OPEN"));
    }

    @Test
    public void givenAScheduleWithAClosed_WhenTryToOpenSession_Then() {
        int duration = 1;
        val session = buildSession(CLOSED);
        val schedule = buildSchedule(session);

        SessionEndpoints.open(schedule.getId(), duration)
                .statusCode(OK);
    }

    @Test
    public void givenAScheduleWithAnAlreadyOpenedSession_WhenSessionIsOpened_ThenShouldReturnExpectedError() {
        int duration = 1;
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);

        SessionEndpoints.open(schedule.getId(), duration)
                .statusCode(OK);
    }


    /**
     * A new session can be canceled, so this cant be voted.
     */
    @Test
    public void givenANewSession_WhenACloseEventStarted_ThenSessionShouldBeClosed() {
        val session = buildSession(NEW);
        val schedule = buildSchedule(session);
        doNothing().when(sessionClosedProducer).close(schedule.getId());

        val result = SessionEndpoints.close(schedule.getId())
                .statusCode(OK)
                .extract()
                .as(SessionResponse.class);

        assertThat(result.getState()).isEqualTo(CLOSED);
    }

    /**
     * A open session can be closed, so this can't be voted, anymore.
     */
    @Test
    public void givenAOpenSession_WhenACloseEventStarted_ThenSessionShouldBeClosed() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        doNothing().when(sessionClosedProducer).close(schedule.getId());

        val result = SessionEndpoints.close(schedule.getId())
                .statusCode(OK)
                .extract()
                .as(SessionResponse.class);

        assertThat(result.getState()).isEqualTo(CLOSED);
    }

    /**
     * A closed session can't be canceled again.
     */
    @Test
    public void givenAClosedSession_WhenACloseEventStarted_ShouldReturnAnException() {
        val session = buildSession(CLOSED);
        val schedule = buildSchedule(session);
        SessionEndpoints.close(schedule.getId())
                .statusCode(BAD_REQUEST)
                .body("message", equalTo("Session is not in a votable state"));
    }

    /**
     * Success
     */
    @Test
    public void givenAOpenedSession_AndAPersonAbleToVote_WhenThisPersonRequestsAVote_ShouldCanVoteWithSuccess() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        val expectedPerson = buildPersonWithState(true);
        val voteRequest = buildVoteRequest(expectedPerson.getId());

        assertThat(session.getVoteCount()).isEqualTo(0);

        val result = sessionController.vote(schedule.getId(), voteRequest);

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

        val voteRequest = buildVoteRequest(expectedPerson.getId());

        List<Vote> votes = Collections.singletonList(Vote.builder().person(expectedPerson).vote(YES).build());
        session.setVotes(votes);

        assertThat(session.getVoteCount()).isEqualTo(1);

        SessionEndpoints.vote(voteRequest, schedule.getId())
                .statusCode(BAD_REQUEST);

        assertThat(session.getVoteCount()).isEqualTo(1);
    }

    @Test
    public void givenAOpenedSession_AndAPersonUnableToVote_WhenThisPersonRequestsAVote_ShouldCantVoteWithSuccess() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        val newSession = schedule.getSession();

        val expectedPerson = buildPersonWithState(false);

        val voteRequest = buildVoteRequest(expectedPerson.getId());

        assertThat(newSession.getVoteCount()).isEqualTo(0);

        SessionEndpoints.vote(voteRequest, schedule.getId())
                .statusCode(BAD_REQUEST);

        assertThat(newSession.getVoteCount()).isEqualTo(0);
    }

    @Test
    public void givenAOpenedSession_AndANonExistentPerson_WhenThisPersonRequestsAVote_ShouldReturnAException() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        val newSession = schedule.getSession();

        val voteRequest = buildVoteRequest("13231");

        assertThat(newSession.getVoteCount()).isEqualTo(0);

        SessionEndpoints.vote(voteRequest, schedule.getId())
                .statusCode(BAD_REQUEST);
        //messsage person not found

        assertThat(newSession.getVoteCount()).isEqualTo(0);
    }

    @Test
    public void givenAClosedSession_AndAPersonAbleToVote_WhenThisPersonRequestsAVote_ShouldCant() {
        val session = buildSession(OPEN);
        val schedule = buildSchedule(session);
        val newSession = schedule.getSession();

        val expectedPerson = buildPersonWithState(true);

        val voteRequest = buildVoteRequest(expectedPerson.getId());

        assertThat(newSession.getVoteCount()).isEqualTo(0);

        SessionEndpoints.vote(voteRequest, schedule.getId())
                .statusCode(BAD_REQUEST);

        assertThat(newSession.getVoteCount()).isEqualTo(0);
    }

    @Test
    public void givenANewSession_AndAPersonAbleToVote_WhenThisPersonRequestsAVote_ShouldCantVote() {
        val session = buildSession(NEW);
        val schedule = buildSchedule(session);
        val newSession = schedule.getSession();

        val expectedPerson = buildPersonWithState(false);

        val voteRequest = buildVoteRequest(expectedPerson.getId());

        assertThat(newSession.getVoteCount()).isEqualTo(0);

        SessionEndpoints.vote(voteRequest, schedule.getId())
                .statusCode(BAD_REQUEST);

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
        val result = result(schedule.getId())
                .statusCode(OK)
                .extract()
                .as(SessionResult.class);

        assertThat(result).isNotNull();
        assertThat(result.getNo()).isEqualTo(0);
        assertThat(result.getYes()).isEqualTo(1);
        assertThat(result.getTotal()).isEqualTo(1);
    }

    @Test
    public void givenANotExistentSession_AndRequestsToSessionResult_ShouldReturnAException() {
        val result = result("1111")
                .statusCode(NOT_FOUND)
                .body("message", equalTo("Schedule not found"));
    }

}

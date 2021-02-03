package com.training.schedule.service;

import com.training.schedule.controller.request.SessionVoteRequest;
import com.training.schedule.domain.exception.PersonNotAbleToVoteException;
import com.training.schedule.domain.exception.SessionClosedException;
import com.training.schedule.domain.person.Person;
import com.training.schedule.domain.schedule.session.Session;
import com.training.schedule.domain.schedule.session.Vote;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionVoteService {

    private final ScheduleSessionService sessionService;
    private final PersonService personService;

    public void vote(final SessionVoteRequest sessionVoteRequest) {
        log.info("Person {}, will vote", sessionVoteRequest.getPersonId());
        val currentSchedule = sessionService.findSchedule(sessionVoteRequest.getScheduleId());
        val session = currentSchedule.getSession();
        if (!session.isOpen()) {
            throw new SessionClosedException(format("Session %s, is not open for votes!", currentSchedule.getId()));
        }

        val currentAssociate = personService.findById(sessionVoteRequest.getPersonId());
        validateSession(session, currentAssociate);

        val vote = buildVote(sessionVoteRequest, currentAssociate);
        session.addVote(vote);
        sessionService.save(currentSchedule);
    }

    private Vote buildVote(SessionVoteRequest sessionVoteRequest, Person currentAssociate) {
        return Vote.builder()
            .person(currentAssociate)
            .vote(sessionVoteRequest.getVote())
            .build();
    }

    private void validateSession(final Session currentSession, final Person currentAssociate) {
        if (!currentAssociate.isAble()) {
            throw new PersonNotAbleToVoteException();
        }
        if (currentSession.voted(currentAssociate.getId())) {
            throw new PersonNotAbleToVoteException();
        }
    }
}

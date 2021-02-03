package com.training.schedule.service;

import static com.training.schedule.controller.request.VoteOption.NO;
import static com.training.schedule.controller.request.VoteOption.YES;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.training.schedule.controller.request.SessionResult;

import lombok.RequiredArgsConstructor;
import lombok.val;

@Service
@RequiredArgsConstructor
public class SessionResultService {

    private final ScheduleSessionService scheduleSessionService;

    private final MongoTemplate mongoTemplate;

    public SessionResult getSessionResult(final String scheduleId) {
        val serviceSchedule = scheduleSessionService.findSchedule(scheduleId);

        val session = serviceSchedule.getSession();

        return SessionResult.builder()
                .total(session.getVoteCount())
                .yes((int) session.getVotesOf(YES))
                .no((int) session.getVotesOf(NO))
                .build();
    }
}

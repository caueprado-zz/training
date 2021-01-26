package com.training.schedule.service;

import com.training.schedule.controller.request.SessionResult;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.training.schedule.controller.request.VoteOption.NO;
import static com.training.schedule.controller.request.VoteOption.YES;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public class SessionResultService {

    private final ScheduleSessionService scheduleSessionService;

    private final MongoTemplate mongoTemplate;

    public SessionResult getSessionResult(final String scheduleId) {
        val serviceSchedule = scheduleSessionService.findSchedule(scheduleId);

        val session = serviceSchedule.getSession();

//        val aggregation = Aggregation.newAggregation(
//            project("yes"),
//            unwind("yes"),
//            group("yes").count().as("yes"),
//            project("yes").and("yes").previousOperation(),
//            sort(DESC, "n"));
//
//        AggregationResults<SessionResult> results = mongoTemplate.aggregate(aggregation, "tags", SessionResult.class);
//        List<SessionResult> tagCount = results.getMappedResults();

        return SessionResult.builder()
            .total(session.getVoteCount())
            .yes((int) session.getVotesOf(YES))
            .no((int) session.getVotesOf(NO))
            .build();
    }
}
